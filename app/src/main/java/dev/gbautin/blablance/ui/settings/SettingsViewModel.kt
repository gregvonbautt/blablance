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
}
