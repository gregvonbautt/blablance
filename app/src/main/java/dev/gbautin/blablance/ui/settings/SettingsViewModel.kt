package dev.gbautin.blablance.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.gbautin.blablance.data.ActivityRepository
import dev.gbautin.blablance.ui.home.Activity

class SettingsViewModel : ViewModel() {

    val activities: LiveData<List<Activity>> = ActivityRepository.activities
    val positiveActivities: LiveData<List<Activity>> = ActivityRepository.positiveActivities
    val negativeActivities: LiveData<List<Activity>> = ActivityRepository.negativeActivities

    fun deleteActivity(activity: Activity) {
        ActivityRepository.deleteActivity(activity)
    }

    fun updateActivity(activity: Activity) {
        ActivityRepository.updateActivity(activity)
    }

    fun addActivity(activity: Activity) {
        ActivityRepository.addActivity(activity)
    }

    fun getNextActivityId(): Int {
        val allActivities = activities.value ?: emptyList()
        return (allActivities.maxOfOrNull { it.id } ?: 0) + 1
    }
}
