package dev.gbautin.blablance.data

import dev.gbautin.blablance.ui.home.Activity
import kotlinx.serialization.Serializable

@Serializable
data class AppState(
    val version: Int = 1,
    val score: Int = 0,
    val activities: List<Activity> = emptyList(),
    val activityEntries: List<ActivityEntry> = emptyList(),
    val scoreEvents: List<ScoreEvent> = emptyList()
)
