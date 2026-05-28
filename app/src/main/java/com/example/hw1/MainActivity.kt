package com.example.hw1

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hw1.databinding.ActivityMainBinding
import kotlin.random.Random

/**
 * HW1 — Endless 3-lane obstacle racing game.
 *
 * Requirements implemented:
 *  - Three-lane road
 *  - Car that can move left and right (via on-screen arrow buttons)
 *  - Obstacles on the road
 *  - Constant scrolling speed (obstacles come toward the player)
 *  - Crash notification: Toast message + device vibration
 *  - Three lives (hearts) displayed at the top
 *  - Endless game: after losing 3 lives, lives reset and the game continues
 *
 * Note: per assignment instructions, no Canvas is used — positions are updated
 * via translationX / translationY on regular Views.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // --- Game configuration ---
    private val laneCount = 3
    private val tickMs = 30L                // ~33 fps
    private val obstacleSpeedDp = 6          // dp per tick
    private val spawnIntervalMs = 900L       // new obstacle every ~0.9 s
    private val maxLives = 3

    // --- Game state ---
    private var carLane = 1                  // 0 = left, 1 = center, 2 = right
    private var lives = maxLives
    private val obstacles = mutableListOf<Obstacle>()
    private val hearts: List<ImageView> by lazy {
        listOf(binding.heart1, binding.heart2, binding.heart3)
    }

    // --- Geometry (resolved once the game area is laid out) ---
    private var laneWidthPx = 0f
    private var gameAreaHeightPx = 0
    private var carBaseY = 0f
    private var speedPx = 0f
    private var obstacleWidthPx = 0
    private var obstacleHeightPx = 0
    private var ready = false

    private val mainHandler = Handler(Looper.getMainLooper())

    private val tickRunnable = object : Runnable {
        override fun run() {
            onTick()
            mainHandler.postDelayed(this, tickMs)
        }
    }

    private val spawnRunnable = object : Runnable {
        override fun run() {
            spawnObstacle()
            mainHandler.postDelayed(this, spawnIntervalMs)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLeft.setOnClickListener { moveCar(-1) }
        binding.btnRight.setOnClickListener { moveCar(+1) }

        // Wait for the game area to be measured before we initialise positions.
        binding.gameArea.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.gameArea.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    setupGeometry()
                    placeCar(animated = false)
                    ready = true
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        if (ready) startLoops()
        else binding.gameArea.post {
            if (ready) startLoops()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLoops()
    }

    // --- Setup ---

    private fun setupGeometry() {
        val areaWidth = binding.gameArea.width.toFloat()
        gameAreaHeightPx = binding.gameArea.height
        laneWidthPx = areaWidth / laneCount

        // Position the two lane dividers between the three lanes.
        binding.laneLine1.translationX = laneWidthPx - binding.laneLine1.width / 2f
        binding.laneLine2.translationX = 2 * laneWidthPx - binding.laneLine2.width / 2f

        // Car sits near the bottom of the road, with a small margin.
        val carMargin = dpToPx(24).toFloat()
        carBaseY = (gameAreaHeightPx - binding.car.layoutParams.height - carMargin)

        speedPx = dpToPx(obstacleSpeedDp).toFloat()
        obstacleWidthPx = dpToPx(64)
        obstacleHeightPx = dpToPx(64)
    }

    private fun startLoops() {
        stopLoops()
        mainHandler.postDelayed(tickRunnable, tickMs)
        mainHandler.postDelayed(spawnRunnable, spawnIntervalMs)
    }

    private fun stopLoops() {
        mainHandler.removeCallbacks(tickRunnable)
        mainHandler.removeCallbacks(spawnRunnable)
    }

    // --- Player control ---

    private fun moveCar(direction: Int) {
        if (!ready) return
        val newLane = (carLane + direction).coerceIn(0, laneCount - 1)
        if (newLane == carLane) return
        carLane = newLane
        placeCar(animated = true)
    }

    private fun placeCar(animated: Boolean) {
        val laneCenter = laneCenterX(carLane)
        val targetX = laneCenter - binding.car.layoutParams.width / 2f
        if (animated) {
            binding.car.animate()
                .translationX(targetX)
                .setDuration(120)
                .start()
        } else {
            binding.car.translationX = targetX
        }
        binding.car.translationY = carBaseY
    }

    private fun laneCenterX(lane: Int): Float = laneWidthPx * (lane + 0.5f)

    // --- Obstacle lifecycle ---

    private fun spawnObstacle() {
        if (!ready) return
        val lane = Random.nextInt(laneCount)
        val view = ImageView(this).apply {
            setImageResource(R.drawable.ic_obstacle)
            contentDescription = getString(R.string.cd_obstacle)
            layoutParams = FrameLayout.LayoutParams(
                obstacleWidthPx,
                obstacleHeightPx,
                Gravity.START or Gravity.TOP
            )
            translationX = laneCenterX(lane) - obstacleWidthPx / 2f
            translationY = -obstacleHeightPx.toFloat()
        }
        binding.gameArea.addView(view)
        obstacles.add(Obstacle(view, lane))
    }

    private fun onTick() {
        if (!ready) return
        val iterator = obstacles.iterator()
        while (iterator.hasNext()) {
            val obstacle = iterator.next()
            obstacle.view.translationY += speedPx

            if (collidesWithCar(obstacle)) {
                iterator.remove()
                binding.gameArea.removeView(obstacle.view)
                handleCrash()
                continue
            }
            if (obstacle.view.translationY > gameAreaHeightPx) {
                iterator.remove()
                binding.gameArea.removeView(obstacle.view)
            }
        }
    }

    private fun collidesWithCar(obstacle: Obstacle): Boolean {
        if (obstacle.lane != carLane) return false
        val obstacleTop = obstacle.view.translationY
        val obstacleBottom = obstacleTop + obstacleHeightPx
        val carTop = carBaseY
        val carBottom = carTop + binding.car.layoutParams.height
        return obstacleBottom >= carTop && obstacleTop <= carBottom
    }

    // --- Crash handling ---

    private fun handleCrash() {
        vibrate()
        Toast.makeText(this, R.string.crash_message, Toast.LENGTH_SHORT).show()

        lives--
        updateHearts()

        if (lives <= 0) {
            // Endless: clear obstacles, reset lives, keep playing.
            Toast.makeText(this, R.string.game_over, Toast.LENGTH_SHORT).show()
            resetGame()
        }
    }

    private fun updateHearts() {
        hearts.forEachIndexed { index, view ->
            view.visibility = if (index < lives) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun resetGame() {
        // Remove all live obstacles from the game area.
        for (o in obstacles) binding.gameArea.removeView(o.view)
        obstacles.clear()

        lives = maxLives
        updateHearts()
        carLane = 1
        placeCar(animated = true)
    }

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

    // --- Helpers ---

    private fun dpToPx(dp: Int): Int =
        (dp * resources.displayMetrics.density).toInt()

    private data class Obstacle(val view: ImageView, val lane: Int)
}
