package com.example.hw2

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.hw2.data.HighScore
import com.example.hw2.data.HighScoreRepository
import com.example.hw2.databinding.ActivityGameBinding
import com.example.hw2.game.GameEngine
import com.example.hw2.game.GameMode
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * HW2 — the game screen. Reuses the HW1 visibility-toggling grid model
 * (no Canvas, no translationX/Y) and adds: a 5-lane / longer road, coins,
 * an odometer, a crash sound, tilt-sensor control, and persistence of the
 * final score (with device location) into Room.
 */
class GameActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityGameBinding
    private lateinit var mode: GameMode
    private lateinit var engine: GameEngine
    private lateinit var repository: HighScoreRepository

    // --- Grid configuration (HW2: wider + longer road) ---
    private val rows = 16
    private val cols = 5
    private val maxLives = 3

    private var running = false
    private var scoreSaved = false

    private lateinit var cells: Array<Array<ImageView>>
    private lateinit var cellRes: Array<IntArray>   // currently shown drawable per cell
    private lateinit var carCells: Array<ImageView>

    private var crashToast: Toast? = null
    private var toneGenerator: ToneGenerator? = null

    // --- Sensor state ---
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var lastTiltMoveMs = 0L
    private var speedFactor = 1f     // bonus: tilt back/forth changes speed

    private val hearts: List<ImageView> by lazy {
        listOf(binding.heart1, binding.heart2, binding.heart3)
    }

    private val mainHandler = Handler(Looper.getMainLooper())

    private val tickRunnable = object : Runnable {
        override fun run() {
            if (!running) return
            onTick()
            if (running) mainHandler.postDelayed(this, effectiveTickMs())
        }
    }

    private val locationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* result handled lazily when the game ends */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mode = GameMode.fromName(intent.getStringExtra(GameMode.EXTRA_KEY))
        engine = GameEngine(rows = rows, cols = cols, maxLives = maxLives)
        repository = HighScoreRepository.from(this)

        buildGrid()
        setupControlsForMode()

        binding.btnLeft.setOnClickListener { onMove(-1) }
        binding.btnRight.setOnClickListener { onMove(+1) }
        binding.btnPlayAgain.setOnClickListener { onPlayAgain() }
        binding.btnMenu.setOnClickListener { finish() }

        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 90)
        } catch (_: RuntimeException) {
            toneGenerator = null
        }

        ensureLocationPermission()
        updateHud()
        renderGrid()
    }

    override fun onResume() {
        super.onResume()
        if (binding.gameOverOverlay.visibility == View.VISIBLE) return
        registerSensor()
        startLoop()
    }

    override fun onPause() {
        super.onPause()
        unregisterSensor()
        stopLoop()
    }

    override fun onDestroy() {
        super.onDestroy()
        toneGenerator?.release()
        toneGenerator = null
    }

    // --- Mode wiring ---

    private fun setupControlsForMode() {
        if (mode.usesSensor) {
            binding.btnLeft.visibility = View.GONE
            binding.btnRight.visibility = View.GONE
            sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
            accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        } else {
            binding.btnLeft.visibility = View.VISIBLE
            binding.btnRight.visibility = View.VISIBLE
        }
    }

    private fun registerSensor() {
        if (!mode.usesSensor) return
        accelerometer?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    private fun unregisterSensor() {
        if (mode.usesSensor) sensorManager?.unregisterListener(this)
    }

    private fun effectiveTickMs(): Long {
        if (!mode.usesSensor) return mode.tickMs
        return (mode.tickMs / speedFactor).toLong().coerceIn(80L, 600L)
    }

    // --- Grid construction ---

    private fun buildGrid() {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        cellRes = Array(rows) { IntArray(cols) }
        cells = Array(rows) {
            val rowLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
                )
            }
            val rowCells = Array(cols) {
                val iv = ImageView(this).apply {
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    visibility = View.INVISIBLE
                    layoutParams = LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT, 1f
                    )
                }
                rowLayout.addView(iv)
                iv
            }
            container.addView(rowLayout)
            rowCells
        }

        // Car row (bottom).
        val carRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
            )
        }
        carCells = Array(cols) { c ->
            val iv = ImageView(this).apply {
                setImageResource(R.drawable.ic_car)
                contentDescription = getString(R.string.cd_car)
                scaleType = ImageView.ScaleType.FIT_CENTER
                visibility = if (c == engine.carLane) View.VISIBLE else View.INVISIBLE
                layoutParams = LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 1f
                )
            }
            carRow.addView(iv)
            iv
        }
        container.addView(carRow)

        binding.gameArea.addView(container)
    }

    // --- Game loop ---

    private fun startLoop() {
        stopLoop()
        running = true
        mainHandler.postDelayed(tickRunnable, effectiveTickMs())
    }

    private fun stopLoop() {
        running = false
        mainHandler.removeCallbacks(tickRunnable)
    }

    private fun onTick() {
        val result = engine.tick()

        if (result.coinCollected) {
            playTone(ToneGenerator.TONE_PROP_BEEP, 120)
        }
        if (result.crashed) {
            handleCrash()
        }

        updateHud()
        renderGrid()

        if (result.gameOver) {
            showGameOver()
        }
    }

    private fun renderGrid() {
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val cell = cells[r][c]
                val res = when {
                    engine.obstacleAt(r, c) -> R.drawable.ic_obstacle
                    engine.coinAt(r, c) -> R.drawable.ic_coin
                    else -> 0
                }
                if (cellRes[r][c] != res) {
                    cellRes[r][c] = res
                    if (res == 0) {
                        cell.visibility = View.INVISIBLE
                    } else {
                        cell.setImageResource(res)
                        cell.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun updateHud() {
        binding.txtDistance.text = getString(R.string.distance_format, engine.distance)
        binding.txtCoins.text = getString(R.string.coins_format, engine.coinCount)
    }

    // --- Player control ---

    private fun onMove(direction: Int) {
        val previous = engine.carLane
        engine.moveCar(direction)
        if (engine.carLane != previous) updateCarCell(previous)
    }

    private fun updateCarCell(previousLane: Int) {
        carCells[previousLane].visibility = View.INVISIBLE
        carCells[engine.carLane].visibility = View.VISIBLE
    }

    // --- Sensor input ---

    override fun onSensorChanged(event: SensorEvent) {
        if (!running || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val x = event.values[0]   // left/right tilt
        val y = event.values[1]   // forward/back tilt (bonus: speed)

        // Bonus: tilt back and forth changes the scroll speed.
        speedFactor = (1f + (y / SensorManager.GRAVITY_EARTH) * 0.8f)
            .coerceIn(0.6f, 1.8f)

        val now = System.currentTimeMillis()
        if (abs(x) > TILT_THRESHOLD && now - lastTiltMoveMs > TILT_COOLDOWN_MS) {
            // Tilt right (negative x in portrait) => move right, and vice versa.
            val direction = if (x < 0) +1 else -1
            onMove(direction)
            lastTiltMoveMs = now
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { /* no-op */ }

    // --- Crash, sound & lives ---

    private fun handleCrash() {
        vibrate()
        playTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 250)
        crashToast?.cancel()
        crashToast = Toast.makeText(this, R.string.crash_message, Toast.LENGTH_SHORT).also { it.show() }
        updateHearts()
    }

    private fun updateHearts() {
        hearts.forEachIndexed { index, view ->
            view.visibility = if (index < engine.lives) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun playTone(tone: Int, durationMs: Int) {
        try {
            toneGenerator?.startTone(tone, durationMs)
        } catch (_: RuntimeException) {
            // Some devices can throw if the tone stream is busy; ignore.
        }
    }

    // --- Game over / restart ---

    private fun showGameOver() {
        stopLoop()
        crashToast?.cancel()
        crashToast = null

        binding.txtFinalScore.text = getString(
            R.string.final_score_format,
            engine.score(), engine.distance, engine.coinCount
        )
        binding.gameOverOverlay.visibility = View.VISIBLE

        saveScore()
    }

    private fun onPlayAgain() {
        binding.gameOverOverlay.visibility = View.GONE
        engine.reset()
        scoreSaved = false
        // reset car cells
        for (c in 0 until cols) {
            carCells[c].visibility = if (c == engine.carLane) View.VISIBLE else View.INVISIBLE
        }
        updateHearts()
        updateHud()
        renderGrid()
        registerSensor()
        startLoop()
    }

    // --- Persisting the score with the device location ---

    private fun ensureLocationPermission() {
        if (!hasLocationPermission()) {
            locationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun hasLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED

    private fun saveScore() {
        if (scoreSaved) return
        scoreSaved = true

        val score = engine.score()
        val distance = engine.distance
        val coins = engine.coinCount

        if (!hasLocationPermission()) {
            persist(score, distance, coins, null, null)
            return
        }

        try {
            val client = LocationServices.getFusedLocationProviderClient(this)
            client.lastLocation
                .addOnSuccessListener { loc ->
                    if (loc != null) {
                        persist(score, distance, coins, loc.latitude, loc.longitude)
                    } else {
                        // A cached location isn't always available (e.g. right
                        // after boot); ask for a fresh fix instead.
                        requestCurrentLocation(client, score, distance, coins)
                    }
                }
                .addOnFailureListener {
                    requestCurrentLocation(client, score, distance, coins)
                }
        } catch (_: SecurityException) {
            persist(score, distance, coins, null, null)
        }
    }

    private fun requestCurrentLocation(
        client: FusedLocationProviderClient,
        score: Int,
        distance: Int,
        coins: Int,
    ) {
        try {
            client.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                CancellationTokenSource().token
            )
                .addOnSuccessListener { loc ->
                    persist(score, distance, coins, loc?.latitude, loc?.longitude)
                }
                .addOnFailureListener {
                    persist(score, distance, coins, null, null)
                }
        } catch (_: SecurityException) {
            persist(score, distance, coins, null, null)
        }
    }

    private fun persist(score: Int, distance: Int, coins: Int, lat: Double?, lng: Double?) {
        val entry = HighScore(
            score = score,
            distance = distance,
            coins = coins,
            latitude = lat ?: 0.0,
            longitude = lng ?: 0.0,
            hasLocation = lat != null && lng != null,
            timestamp = System.currentTimeMillis(),
        )
        lifecycleScope.launch {
            repository.add(entry)
        }
    }

    // --- Vibration helper ---

    private fun vibrate() {
        val durationMs = 250L
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator.vibrate(
                VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(durationMs)
            }
        }
    }

    companion object {
        private const val TILT_THRESHOLD = 2.2f
        private const val TILT_COOLDOWN_MS = 220L
    }
}
