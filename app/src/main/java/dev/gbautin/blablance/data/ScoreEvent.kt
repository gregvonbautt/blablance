package dev.gbautin.blablance.data

import java.time.LocalDateTime

data class ScoreEvent(
    val buttonTitle: String,
    val scoreDelta: Int,
    val timestamp: LocalDateTime = LocalDateTime.now()
)