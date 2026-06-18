package com.example.hw2.game

/**
 * How the player controls the car and how fast the road scrolls.
 *
 * The menu offers three options (HW2 spec):
 *  - two on-screen buttons, slow pace,
 *  - two on-screen buttons, fast pace,
 *  - tilt sensor (accelerometer).
 */
enum class GameMode(val tickMs: Long, val usesSensor: Boolean) {
    BUTTONS_SLOW(tickMs = 320L, usesSensor = false),
    BUTTONS_FAST(tickMs = 170L, usesSensor = false),
    SENSOR(tickMs = 240L, usesSensor = true);

    companion object {
        const val EXTRA_KEY = "com.example.hw2.GAME_MODE"

        fun fromName(name: String?): GameMode =
            entries.firstOrNull { it.name == name } ?: BUTTONS_SLOW
    }
}
