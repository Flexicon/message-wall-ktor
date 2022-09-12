package com.flexicondev.messagewall.http.responses

import com.flexicondev.messagewall.domain.Message
import com.flexicondev.messagewall.serializers.InstantAsStringSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneOffset

@Serializable
data class MessageResponse(
    val id: String,
    val text: String,
    val author: String,
    @Serializable(with = InstantAsStringSerializer::class)
    val timestamp: Instant,
) {
    companion object {
        fun from(message: Message) = MessageResponse(
            message.id.toString(),
            message.text,
            message.author,
            message.createdAt.toInstant(ZoneOffset.UTC)
        )
    }
}
