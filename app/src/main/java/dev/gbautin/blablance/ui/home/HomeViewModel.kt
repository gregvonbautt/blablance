package dev.gbautin.blablance.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.gbautin.blablance.data.ActivityEntry
import dev.gbautin.blablance.data.ActivityRepository

data class ScoreButton(
    val title: String,
    val scoreDelta: Int
)

data class Activity(
    val id: Int,
    val name: String,
    val description: String,
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

    val positiveActivities: LiveData<List<Activity>> = ActivityRepository.positiveActivities

    val negativeActivities: LiveData<List<Activity>> = ActivityRepository.negativeActivities

    private val _activityEntries = MutableLiveData<List<ActivityEntry>>().apply {
        value = emptyList()
    }
    val activityEntries: LiveData<List<ActivityEntry>> = _activityEntries

    fun adjustScore(delta: Int) {
        _score.value = (_score.value ?: 0) + delta
    }

    fun addActivityEntry(activity: Activity) {
        val entry = ActivityEntry(
            name = activity.name,
            description = activity.description,
            scoreDelta = activity.scoreDelta
        )
        val currentEntries = _activityEntries.value ?: emptyList()
        _activityEntries.value = listOf(entry) + currentEntries
        adjustScore(activity.scoreDelta)
    }

    fun removeActivityEntry(entryId: String) {
        val currentEntries = _activityEntries.value ?: emptyList()
        val entryToRemove = currentEntries.find { it.id == entryId }
        if (entryToRemove != null) {
            _activityEntries.value = currentEntries.filter { it.id != entryId }
            adjustScore(-entryToRemove.scoreDelta)
        }
    }

    fun incrementScore() {
        _score.value = (_score.value ?: 0) + 1
    }

    fun decrementScore() {
        _score.value = (_score.value ?: 0) - 1
    }
}