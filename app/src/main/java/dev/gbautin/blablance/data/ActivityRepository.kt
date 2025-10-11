package dev.gbautin.blablance.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.gbautin.blablance.ui.home.Activity

object ActivityRepository {

    private val _activities = MutableLiveData<List<Activity>>().apply {
        value = listOf(
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
    }

    val activities: LiveData<List<Activity>> = _activities

    val positiveActivities: LiveData<List<Activity>> = MutableLiveData<List<Activity>>().apply {
        _activities.observeForever { allActivities ->
            value = allActivities
                .filter { it.scoreDelta > 0 }
                .sortedByDescending { it.scoreDelta }
        }
    }

    val negativeActivities: LiveData<List<Activity>> = MutableLiveData<List<Activity>>().apply {
        _activities.observeForever { allActivities ->
            value = allActivities
                .filter { it.scoreDelta < 0 }
                .sortedBy { it.scoreDelta }
        }
    }

    fun deleteActivity(activity: Activity) {
        val currentActivities = _activities.value ?: emptyList()
        _activities.value = currentActivities.filter { it.id != activity.id }
    }

    fun addActivity(activity: Activity) {
        val currentActivities = _activities.value ?: emptyList()
        _activities.value = currentActivities + activity
    }

    fun updateActivity(activity: Activity) {
        val currentActivities = _activities.value ?: emptyList()
        _activities.value = currentActivities.map {
            if (it.id == activity.id) activity else it
        }
    }
}
