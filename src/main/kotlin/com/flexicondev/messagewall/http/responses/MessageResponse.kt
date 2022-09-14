package com.flexicondev.messagewall.http.responses

import com.flexicondev.messagewall.serializers.InstantAsStringSerializer
import kotlinx.serialization.Serializable
import org.joda.time.Instant

@Serializable
data class MessageResponse(
    val id: String,
    val text: String,
    val author: String,
    @Serializable(with = InstantAsStringSerializer::class)
    val timestamp: Instant,
)
