package dev.gbautin.blablance.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gbautin.blablance.data.ActivityEntry
import dev.gbautin.blablance.data.ActivityRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

data class ScoreButton(
    val title: String,
    val scoreDelta: Int
)

@Serializable
data class Activity(
    val id: Int,
    val name: String,
    val description: String,
    val scoreDelta: Int
)

class HomeViewModel : ViewModel() {

    private val dataStore = dev.gbautin.blablance.BlablanceApplication.getInstance().dataStoreManager

    private val _score = MutableLiveData<Int>()
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

    val positiveActivities: LiveData<List<Activity>> = ActivityRepository.positiveActivities

    val negativeActivities: LiveData<List<Activity>> = ActivityRepository.negativeActivities

    private val _activityEntries = MutableLiveData<List<ActivityEntry>>()
    val activityEntries: LiveData<List<ActivityEntry>> = _activityEntries

    init {
        // Load state from DataStore
        viewModelScope.launch {
            dataStore.appStateFlow.collect { state ->
                _score.postValue(state.score)
                _activityEntries.postValue(state.activityEntries)
            }
        }
    }

    fun adjustScore(delta: Int) {
        viewModelScope.launch {
            val currentState = dataStore.getAppState()
            val newScore = currentState.score + delta
            dataStore.updateScore(newScore)
        }
    }

    fun addActivityEntry(activity: Activity) {
        viewModelScope.launch {
            val currentState = dataStore.getAppState()
            val entry = ActivityEntry(
                name = activity.name,
                description = activity.description,
                scoreDelta = activity.scoreDelta
            )
            val updatedEntries = listOf(entry) + currentState.activityEntries
            val newScore = currentState.score + activity.scoreDelta

            dataStore.saveAppState(
                currentState.copy(
                    activityEntries = updatedEntries,
                    score = newScore
                )
            )
        }
    }

    fun removeActivityEntry(entryId: String) {
        viewModelScope.launch {
            val currentState = dataStore.getAppState()
            val entryToRemove = currentState.activityEntries.find { it.id == entryId }
            if (entryToRemove != null) {
                val updatedEntries = currentState.activityEntries.filter { it.id != entryId }
                val newScore = currentState.score - entryToRemove.scoreDelta

                dataStore.saveAppState(
                    currentState.copy(
                        activityEntries = updatedEntries,
                        score = newScore
                    )
                )
            }
        }
    }

    fun incrementScore() {
        adjustScore(1)
    }

    fun decrementScore() {
        adjustScore(-1)
    }

    fun clearAllEntries() {
        viewModelScope.launch {
            val currentState = dataStore.getAppState()
            dataStore.saveAppState(
                currentState.copy(
                    activityEntries = emptyList(),
                    score = 0
                )
            )
        }
    }
}