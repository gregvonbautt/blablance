package dev.gbautin.blablance.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import dev.gbautin.blablance.BlablanceApplication
import dev.gbautin.blablance.ui.home.Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object ActivityRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val dataStore = BlablanceApplication.getInstance().dataStoreManager

    private val _activities = MutableLiveData<List<Activity>>()
    val activities: LiveData<List<Activity>> = _activities

    private val _positiveActivities = MutableLiveData<List<Activity>>()
    val positiveActivities: LiveData<List<Activity>> = _positiveActivities

    private val _negativeActivities = MutableLiveData<List<Activity>>()
    val negativeActivities: LiveData<List<Activity>> = _negativeActivities

    init {
        // Load activities from DataStore
        scope.launch {
            dataStore.appStateFlow.collect { state ->
                _activities.postValue(state.activities)
                updateFilteredActivities(state.activities)
            }
        }
    }

    private fun updateFilteredActivities(allActivities: List<Activity>) {
        _positiveActivities.postValue(
            allActivities
                .filter { it.scoreDelta > 0 }
                .sortedByDescending { it.scoreDelta }
        )
        _negativeActivities.postValue(
            allActivities
                .filter { it.scoreDelta < 0 }
                .sortedBy { it.scoreDelta }
        )
    }

    fun deleteActivity(activity: Activity) {
        scope.launch {
            val currentActivities = _activities.value ?: emptyList()
            val updatedActivities = currentActivities.filter { it.id != activity.id }
            dataStore.updateActivities(updatedActivities)
        }
    }

    fun addActivity(activity: Activity) {
        scope.launch {
            val currentActivities = _activities.value ?: emptyList()
            val updatedActivities = currentActivities + activity
            dataStore.updateActivities(updatedActivities)
        }
    }

    fun updateActivity(activity: Activity) {
        scope.launch {
            val currentActivities = _activities.value ?: emptyList()
            val updatedActivities = currentActivities.map {
                if (it.id == activity.id) activity else it
            }
            dataStore.updateActivities(updatedActivities)
        }
    }
}
