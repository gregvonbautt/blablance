package dev.gbautin.blablance.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class ScoreButton(
    val title: String,
    val scoreDelta: Int
)

class HomeViewModel : ViewModel() {

    private val _score = MutableLiveData<Int>().apply {
        value = 0
    }
    val score: LiveData<Int> = _score

    private val _scoreButtons = listOf(
        ScoreButton("Bad Day", -5),
        ScoreButton("Meh", -1),
        ScoreButton("Good", 1),
        ScoreButton("Great Day", 3),
        ScoreButton("Amazing", 5)
    )
    val scoreButtons: List<ScoreButton> = _scoreButtons

    val negativeButtons: List<ScoreButton>
        get() = _scoreButtons.filter { it.scoreDelta < 0 }

    val positiveButtons: List<ScoreButton>
        get() = _scoreButtons.filter { it.scoreDelta > 0 }

    fun adjustScore(delta: Int) {
        _score.value = (_score.value ?: 0) + delta
    }

    fun incrementScore() {
        _score.value = (_score.value ?: 0) + 1
    }

    fun decrementScore() {
        _score.value = (_score.value ?: 0) - 1
    }
}