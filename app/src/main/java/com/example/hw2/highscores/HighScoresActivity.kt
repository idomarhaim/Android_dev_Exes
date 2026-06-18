package com.example.hw2.highscores

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hw2.databinding.ActivityHighScoresBinding

/**
 * Hosts the two high-score fragments: a table of the top-ten scores and a map
 * of the locations where they were achieved. The fragments coordinate through
 * a shared [HighScoresViewModel].
 */
class HighScoresActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHighScoresBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHighScoresBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener { finish() }
    }
}
