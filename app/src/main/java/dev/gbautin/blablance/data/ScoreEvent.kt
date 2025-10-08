package dev.gbautin.blablance.data

import java.time.LocalDateTime
import java.util.UUID

data class ScoreEvent(
    val buttonTitle: String,
    val scoreDelta: Int,
    val timestamp: LocalDateTime = LocalDateTime.now()
)

data class ActivityEntry(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val scoreDelta: Int,
    val timestamp: LocalDateTime = LocalDateTime.now()
)