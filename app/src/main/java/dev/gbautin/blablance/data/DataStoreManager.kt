package dev.gbautin.blablance.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.gbautin.blablance.ui.home.Activity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "blablance_data")

class DataStoreManager(private val context: Context) {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
    }

    companion object {
        private const val CURRENT_VERSION = 1
        private val APP_STATE_KEY = stringPreferencesKey("app_state")

        // Default activities
        private val DEFAULT_ACTIVITIES = listOf(
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

    val appStateFlow: Flow<AppState> = context.dataStore.data.map { preferences ->
        val stateJson = preferences[APP_STATE_KEY]
        if (stateJson != null) {
            val state = json.decodeFromString<AppState>(stateJson)
            migrateIfNeeded(state)
        } else {
            // Return default state with default activities
            AppState(version = CURRENT_VERSION, activities = DEFAULT_ACTIVITIES)
        }
    }

    suspend fun getAppState(): AppState {
        return appStateFlow.first()
    }

    suspend fun saveAppState(state: AppState) {
        context.dataStore.edit { preferences ->
            preferences[APP_STATE_KEY] = json.encodeToString(state)
        }
    }

    /**
     * Migrates the app state to the current version if needed.
     * Add migration logic here for future version upgrades.
     */
    private suspend fun migrateIfNeeded(state: AppState): AppState {
        if (state.version >= CURRENT_VERSION) {
            return state
        }

        var migratedState = state

        // Future migrations can be added here as a chain:
        // if (migratedState.version < 2) {
        //     migratedState = migrateToVersion2(migratedState)
        // }
        // if (migratedState.version < 3) {
        //     migratedState = migrateToVersion3(migratedState)
        // }

        // Update version to current and save
        migratedState = migratedState.copy(version = CURRENT_VERSION)
        saveAppState(migratedState)

        return migratedState
    }

    suspend fun updateScore(newScore: Int) {
        val currentState = getAppState()
        saveAppState(currentState.copy(score = newScore))
    }

    suspend fun updateActivities(activities: List<Activity>) {
        val currentState = getAppState()
        saveAppState(currentState.copy(activities = activities))
    }

    suspend fun updateActivityEntries(entries: List<ActivityEntry>) {
        val currentState = getAppState()
        saveAppState(currentState.copy(activityEntries = entries))
    }

    suspend fun updateScoreEvents(events: List<ScoreEvent>) {
        val currentState = getAppState()
        saveAppState(currentState.copy(scoreEvents = events))
    }

    suspend fun clearAllData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
