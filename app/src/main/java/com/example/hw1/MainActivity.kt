package com.example.hw1

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
import androidx.appcompat.app.AppCompatActivity
import com.example.hw1.databinding.ActivityMainBinding
import kotlin.random.Random

/**
 * HW1 — Endless 3-lane obstacle racing game.
 *
 * Movement model (per assignment / lecturer guidance):
 *  - No translationX / translationY, no Canvas.
 *  - The road is a fixed matrix of [rows] x [cols] ImageView cells plus a
 *    bottom row of car cells. "Movement" is achieved by toggling each cell's
 *    visibility every tick, so the picture appears to jump from one cell to
 *    the next.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // --- Grid configuration ---
    private val rows = 12                    // number of obstacle rows
    private val cols = 3                     // number of lanes
    private val tickMs = 350L                // one row of movement per tick
    private val spawnChance = 0.55f          // chance of a new obstacle per tick
    private val maxLives = 3

    // --- Game state ---
    private var carLane = 1                  // 0 = left, 1 = center, 2 = right
    private var lives = maxLives

    private lateinit var obstacleCells: Array<Array<ImageView>>
    private lateinit var obstacleState: Array<BooleanArray>
    private lateinit var carCells: Array<ImageView>

    private val hearts: List<ImageView> by lazy {
        listOf(binding.heart1, binding.heart2, binding.heart3)
    }

    private val mainHandler = Handler(Looper.getMainLooper())

    private val tickRunnable = object : Runnable {
        override fun run() {
            onTick()
            mainHandler.postDelayed(this, tickMs)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buildGrid()

        binding.btnLeft.setOnClickListener { moveCar(-1) }
        binding.btnRight.setOnClickListener { moveCar(+1) }
        binding.btnPlayAgain.setOnClickListener { onPlayAgain() }
    }

    override fun onResume() {
        super.onResume()
        if (binding.gameOverOverlay.visibility == View.VISIBLE) return
        startLoop()
    }

    override fun onPause() {
        super.onPause()
        stopLoop()
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

        obstacleState = Array(rows) { BooleanArray(cols) }
        obstacleCells = Array(rows) {
            val rowLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
                )
            }
            val cells = Array(cols) {
                val iv = ImageView(this).apply {
                    setImageResource(R.drawable.ic_obstacle)
                    contentDescription = getString(R.string.cd_obstacle)
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
            cells
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
                visibility = if (c == carLane) View.VISIBLE else View.INVISIBLE
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
        mainHandler.postDelayed(tickRunnable, tickMs)
    }

    private fun stopLoop() {
        mainHandler.removeCallbacks(tickRunnable)
    }

    private fun onTick() {
        // 1. Collision: an obstacle currently in the bottom row, in the car's lane.
        if (obstacleState[rows - 1][carLane]) {
            obstacleState[rows - 1][carLane] = false
            handleCrash()
            if (binding.gameOverOverlay.visibility == View.VISIBLE) {
                renderGrid()
                return
            }
        }

        // 2. Shift every row one step down.
        for (r in rows - 1 downTo 1) {
            for (c in 0 until cols) {
                obstacleState[r][c] = obstacleState[r - 1][c]
            }
        }

        // 3. Spawn at the top row.
        for (c in 0 until cols) obstacleState[0][c] = false
        if (Random.nextFloat() < spawnChance) {
            val lane = Random.nextInt(cols)
            obstacleState[0][lane] = true
        }

        // 4. Re-render.
        renderGrid()
    }

    private fun renderGrid() {
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                obstacleCells[r][c].visibility =
                    if (obstacleState[r][c]) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    // --- Player control ---

    private fun moveCar(direction: Int) {
        val newLane = (carLane + direction).coerceIn(0, cols - 1)
        if (newLane == carLane) return
        carCells[carLane].visibility = View.INVISIBLE
        carLane = newLane
        carCells[carLane].visibility = View.VISIBLE
    }

    // --- Crash & lives ---

    private fun handleCrash() {
        vibrate()
        Toast.makeText(this, R.string.crash_message, Toast.LENGTH_SHORT).show()

        lives--
        updateHearts()

        if (lives <= 0) {
            showGameOver()
        }
    }

    private fun updateHearts() {
        hearts.forEachIndexed { index, view ->
            view.visibility = if (index < lives) View.VISIBLE else View.INVISIBLE
        }
    }

    // --- Game over / restart ---

    private fun showGameOver() {
        stopLoop()
        for (r in 0 until rows) for (c in 0 until cols) obstacleState[r][c] = false
        binding.gameOverOverlay.visibility = View.VISIBLE
    }

    private fun onPlayAgain() {
        binding.gameOverOverlay.visibility = View.GONE
        resetGame()
        startLoop()
    }

    private fun resetGame() {
        for (r in 0 until rows) for (c in 0 until cols) obstacleState[r][c] = false
        renderGrid()

        lives = maxLives
        updateHearts()

        for (c in 0 until cols) {
            carCells[c].visibility = if (c == 1) View.VISIBLE else View.INVISIBLE
        }
        carLane = 1
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
}
