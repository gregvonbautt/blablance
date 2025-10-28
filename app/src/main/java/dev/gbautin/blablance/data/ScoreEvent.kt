package dev.gbautin.blablance.data

import dev.gbautin.blablance.data.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class ScoreEvent(
    val buttonTitle: String,
    val scoreDelta: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val timestamp: LocalDateTime = LocalDateTime.now()
)

@Serializable
data class ActivityEntry(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val scoreDelta: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val timestamp: LocalDateTime = LocalDateTime.now()
)