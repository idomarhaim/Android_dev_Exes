package com.example.hw2.highscores

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
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

        // The Google map can only render with a real API key. If it's still the
        // placeholder, explain that instead of showing a mysterious blank map.
        if (isMapsKeyMissing()) {
            binding.txtMapUnavailable.visibility = View.VISIBLE
        }
    }

    private fun isMapsKeyMissing(): Boolean {
        val key = try {
            packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
                .metaData?.getString("com.google.android.geo.API_KEY")
        } catch (_: PackageManager.NameNotFoundException) {
            null
        }
        return key.isNullOrBlank() || key == "YOUR_API_KEY_HERE"
    }
}
