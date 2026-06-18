package com.example.hw2.game

import kotlin.random.Random

/**
 * Pure game logic for the endless racing game — no Android dependencies, so it
 * can be unit-tested on the JVM.
 *
 * Movement model (per assignment / lecturer guidance): the road is a fixed
 * [rows] x [cols] matrix of cells. This class only owns the *state* of those
 * cells (which hold an obstacle / a coin) and the car lane; the Activity
 * renders the state by toggling `ImageView` visibility. No coordinates, no
 * `Canvas`, no `translationX/Y`.
 *
 * Each [tick]:
 *  1. advances the odometer,
 *  2. resolves whatever reached the car's row (obstacle => crash, coin => pick up),
 *  3. shifts every row one step down,
 *  4. spawns new obstacles/coins in the top row.
 */
class GameEngine(
    val rows: Int = 16,
    val cols: Int = 5,
    private val maxLives: Int = 3,
    private val obstacleSpawnChance: Float = 0.45f,
    private val coinSpawnChance: Float = 0.20f,
    private val random: Random = Random.Default,
) {
    init {
        require(rows > 1) { "rows must be > 1" }
        require(cols > 0) { "cols must be > 0" }
    }

    private val obstacles: Array<BooleanArray> = Array(rows) { BooleanArray(cols) }
    private val coins: Array<BooleanArray> = Array(rows) { BooleanArray(cols) }

    var carLane: Int = cols / 2
        private set
    var lives: Int = maxLives
        private set
    var distance: Int = 0
        private set
    var coinCount: Int = 0
        private set

    val isGameOver: Boolean get() = lives <= 0

    fun obstacleAt(row: Int, col: Int): Boolean = obstacles[row][col]
    fun coinAt(row: Int, col: Int): Boolean = coins[row][col]

    /** Combined score = distance travelled plus a bonus per collected coin. */
    fun score(): Int = distance + coinCount * COIN_BONUS

    /** Move the car one lane left (-1) or right (+1); clamped to the road. */
    fun moveCar(direction: Int) {
        setLane(carLane + direction)
    }

    /** Place the car directly on [lane] (used by the tilt sensor); clamped. */
    fun setLane(lane: Int) {
        carLane = lane.coerceIn(0, cols - 1)
    }

    /**
     * Advance the game by one step and report what happened to the car.
     * When the game is already over this is a no-op.
     */
    fun tick(): TickResult {
        if (isGameOver) return TickResult.NONE

        distance++

        var crashed = false
        var coinCollected = false

        val bottom = rows - 1
        if (obstacles[bottom][carLane]) {
            obstacles[bottom][carLane] = false
            lives--
            crashed = true
        } else if (coins[bottom][carLane]) {
            coins[bottom][carLane] = false
            coinCount++
            coinCollected = true
        }

        if (isGameOver) {
            return TickResult(crashed = crashed, coinCollected = coinCollected, gameOver = true)
        }

        shiftDown(obstacles)
        shiftDown(coins)
        spawnTopRow()

        return TickResult(crashed = crashed, coinCollected = coinCollected, gameOver = false)
    }

    /** Restore the engine to a fresh game. */
    fun reset() {
        for (r in 0 until rows) {
            obstacles[r].fill(false)
            coins[r].fill(false)
        }
        carLane = cols / 2
        lives = maxLives
        distance = 0
        coinCount = 0
    }

    private fun shiftDown(grid: Array<BooleanArray>) {
        for (r in rows - 1 downTo 1) {
            grid[r] = grid[r - 1].copyOf()
        }
        grid[0] = BooleanArray(cols)
    }

    private fun spawnTopRow() {
        val top = 0
        obstacles[top].fill(false)
        coins[top].fill(false)

        if (random.nextFloat() < obstacleSpawnChance) {
            obstacles[top][random.nextInt(cols)] = true
        }
        if (random.nextFloat() < coinSpawnChance) {
            val lane = random.nextInt(cols)
            // Never stack a coin on an obstacle.
            if (!obstacles[top][lane]) {
                coins[top][lane] = true
            }
        }
    }

    companion object {
        /** Points awarded per coin in the combined score. */
        const val COIN_BONUS = 25
    }
}

/** What a single [GameEngine.tick] did to the player. */
data class TickResult(
    val crashed: Boolean,
    val coinCollected: Boolean,
    val gameOver: Boolean,
) {
    companion object {
        val NONE = TickResult(crashed = false, coinCollected = false, gameOver = false)
    }
}
