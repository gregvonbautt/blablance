package dev.gbautin.blablance.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object EventManager {
    
    private val _events = MutableLiveData<List<ScoreEvent>>().apply {
        value = emptyList()
    }
    val events: LiveData<List<ScoreEvent>> = _events
    
    fun logEvent(buttonTitle: String, scoreDelta: Int) {
        val newEvent = ScoreEvent(buttonTitle, scoreDelta)
        val currentEvents = _events.value ?: emptyList()
        // Add new event at the beginning (latest events on top)
        _events.value = listOf(newEvent) + currentEvents
    }
    
    fun clearEvents() {
        _events.value = emptyList()
    }
}