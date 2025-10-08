package dev.gbautin.blablance.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.gbautin.blablance.data.ActivityEntry

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

    private val _activities = listOf(
        Activity(1, "Exercise", "Physical workout or sports", 3),
        Activity(2, "Meditation", "Mindfulness and relaxation", 2),
        Activity(3, "Reading", "Learning and entertainment", 2),
        Activity(4, "Cooking", "Preparing healthy meals", 1),
        Activity(5, "Social Time", "Quality time with friends/family", 3),
        Activity(6, "Learning", "Study or skill development", 2),
        Activity(7, "Social Media", "Scrolling through feeds", -1),
        Activity(8, "Junk Food", "Eating unhealthy snacks", -2),
        Activity(9, "Procrastination", "Avoiding important tasks", -2),
        Activity(10, "Oversleeping", "Sleeping too much", -1),
        Activity(11, "Negative News", "Consuming depressing content", -2),
        Activity(12, "Argument", "Unproductive conflicts", -3)
    )

    val negativeButtons: List<ScoreButton>
        get() = _scoreButtons.filter { it.scoreDelta < 0 }

    val positiveButtons: List<ScoreButton>
        get() = _scoreButtons.filter { it.scoreDelta > 0 }

    val positiveActivities: List<Activity>
        get() = _activities.filter { it.scoreDelta > 0 }.sortedBy { kotlin.math.abs(it.scoreDelta) }

    val negativeActivities: List<Activity>
        get() = _activities.filter { it.scoreDelta < 0 }.sortedBy { kotlin.math.abs(it.scoreDelta) }

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