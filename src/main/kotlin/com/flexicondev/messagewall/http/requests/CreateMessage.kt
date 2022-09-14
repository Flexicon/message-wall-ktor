package com.flexicondev.messagewall.http.requests

import com.flexicondev.messagewall.domain.Message
import kotlinx.serialization.Serializable

@Serializable
data class CreateMessage(val text: String, val author: String) {
    fun toMessage() = Message(text = text, author = author)
}
