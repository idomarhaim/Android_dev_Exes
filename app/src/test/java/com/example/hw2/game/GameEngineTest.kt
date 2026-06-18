package com.example.hw2.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pure-JVM unit tests for [GameEngine]. The collision/coin scenarios use a
 * single-lane road with a 100% spawn chance, which makes the grid fully
 * deterministic without needing to seed the RNG.
 */
class GameEngineTest {

    @Test
    fun `initial state is centred, full lives, zero score`() {
        val engine = GameEngine(rows = 16, cols = 5)
        assertEquals(2, engine.carLane) // center of 5 lanes
        assertEquals(3, engine.lives)
        assertEquals(0, engine.distance)
        assertEquals(0, engine.coinCount)
        assertEquals(0, engine.score())
        assertFalse(engine.isGameOver)
    }

    @Test
    fun `moveCar is clamped to the road`() {
        val engine = GameEngine(rows = 8, cols = 3)
        engine.moveCar(-1)
        assertEquals(0, engine.carLane)
        engine.moveCar(-1) // already at the edge
        assertEquals(0, engine.carLane)
        engine.moveCar(+1)
        engine.moveCar(+1)
        engine.moveCar(+1) // past the right edge
        assertEquals(2, engine.carLane)
    }

    @Test
    fun `setLane clamps out-of-range lanes`() {
        val engine = GameEngine(rows = 8, cols = 5)
        engine.setLane(99)
        assertEquals(4, engine.carLane)
        engine.setLane(-5)
        assertEquals(0, engine.carLane)
    }

    @Test
    fun `tick advances the odometer`() {
        val engine = GameEngine(rows = 6, cols = 1, obstacleSpawnChance = 0f, coinSpawnChance = 0f)
        repeat(5) { engine.tick() }
        assertEquals(5, engine.distance)
    }

    @Test
    fun `obstacle reaching the car costs a life`() {
        val rows = 5
        val engine = GameEngine(
            rows = rows, cols = 1,
            obstacleSpawnChance = 1f, coinSpawnChance = 0f,
        )
        // No collision until the first obstacle has scrolled to the bottom row.
        repeat(rows) {
            val result = engine.tick()
            assertFalse(result.crashed)
        }
        assertEquals(3, engine.lives)

        val crashTick = engine.tick()
        assertTrue(crashTick.crashed)
        assertEquals(2, engine.lives)
    }

    @Test
    fun `three crashes end the game`() {
        val rows = 4
        val engine = GameEngine(
            rows = rows, cols = 1,
            obstacleSpawnChance = 1f, coinSpawnChance = 0f,
        )
        var result = TickResult.NONE
        // Drive until game over (bounded to avoid an infinite loop on a bug).
        repeat(rows + 5) { if (!engine.isGameOver) result = engine.tick() }
        assertTrue(engine.isGameOver)
        assertEquals(0, engine.lives)
        assertTrue(result.gameOver)
    }

    @Test
    fun `coins are collected and counted in the score`() {
        val rows = 4
        val engine = GameEngine(
            rows = rows, cols = 1,
            obstacleSpawnChance = 0f, coinSpawnChance = 1f,
        )
        repeat(rows) { assertFalse(engine.tick().coinCollected) }
        val pickup = engine.tick()
        assertTrue(pickup.coinCollected)
        assertEquals(1, engine.coinCount)
        assertFalse(engine.isGameOver) // coins never end the game
        assertEquals(engine.distance + GameEngine.COIN_BONUS, engine.score())
    }

    @Test
    fun `reset restores a fresh game`() {
        val engine = GameEngine(rows = 4, cols = 1, obstacleSpawnChance = 1f, coinSpawnChance = 0f)
        repeat(10) { if (!engine.isGameOver) engine.tick() }
        engine.moveCar(+1)

        engine.reset()
        assertEquals(0, engine.distance)
        assertEquals(0, engine.coinCount)
        assertEquals(3, engine.lives)
        assertEquals(0, engine.carLane) // center of a single lane
        assertFalse(engine.isGameOver)
    }
}
