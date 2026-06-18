package com.example.hw2.highscores

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.hw2.data.HighScore
import com.example.hw2.data.HighScoreRepository

/**
 * Shared between [ScoresTableFragment] and [ScoresMapFragment] (activity
 * scope) so that tapping a row in the table moves the map.
 */
class HighScoresViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = HighScoreRepository.from(app)

    val scores: LiveData<List<HighScore>> = repository.topTen.asLiveData()

    private val _selected = MutableLiveData<HighScore?>(null)
    val selected: LiveData<HighScore?> = _selected

    fun select(score: HighScore) {
        _selected.value = score
    }
}
