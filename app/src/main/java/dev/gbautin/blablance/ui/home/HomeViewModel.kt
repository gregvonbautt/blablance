package dev.gbautin.blablance.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _score = MutableLiveData<Int>().apply {
        value = 0
    }
    val score: LiveData<Int> = _score

    fun incrementScore() {
        _score.value = (_score.value ?: 0) + 1
    }

    fun decrementScore() {
        _score.value = (_score.value ?: 0) - 1
    }
}