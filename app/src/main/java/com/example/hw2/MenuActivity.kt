package com.example.hw2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hw2.databinding.ActivityMenuBinding
import com.example.hw2.game.GameMode
import com.example.hw2.highscores.HighScoresActivity

/**
 * Launcher screen. Lets the player pick one of the three control modes or
 * open the high-scores screen.
 */
class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnButtonsSlow.setOnClickListener { startGame(GameMode.BUTTONS_SLOW) }
        binding.btnButtonsFast.setOnClickListener { startGame(GameMode.BUTTONS_FAST) }
        binding.btnSensor.setOnClickListener { startGame(GameMode.SENSOR) }
        binding.btnHighScores.setOnClickListener {
            startActivity(Intent(this, HighScoresActivity::class.java))
        }
    }

    private fun startGame(mode: GameMode) {
        val intent = Intent(this, GameActivity::class.java)
            .putExtra(GameMode.EXTRA_KEY, mode.name)
        startActivity(intent)
    }
}
