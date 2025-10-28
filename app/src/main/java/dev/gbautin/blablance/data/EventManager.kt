package dev.gbautin.blablance.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.gbautin.blablance.BlablanceApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object EventManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val dataStore = BlablanceApplication.getInstance().dataStoreManager

    private val _events = MutableLiveData<List<ScoreEvent>>()
    val events: LiveData<List<ScoreEvent>> = _events

    init {
        // Load events from DataStore
        scope.launch {
            dataStore.appStateFlow.collect { state ->
                _events.postValue(state.scoreEvents)
            }
        }
    }

    fun logEvent(buttonTitle: String, scoreDelta: Int) {
        val newEvent = ScoreEvent(buttonTitle, scoreDelta)
        val currentEvents = _events.value ?: emptyList()
        // Add new event at the beginning (latest events on top)
        val updatedEvents = listOf(newEvent) + currentEvents
        _events.value = updatedEvents
        scope.launch {
            dataStore.updateScoreEvents(updatedEvents)
        }
    }

    fun clearEvents() {
        _events.value = emptyList()
        scope.launch {
            dataStore.updateScoreEvents(emptyList())
        }
    }
}