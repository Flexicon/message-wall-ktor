package com.flexicondev.messagewall.web.requests

import com.flexicondev.messagewall.domain.Message
import com.flexicondev.messagewall.web.validation
import kotlinx.serialization.Serializable

@Serializable
data class CreateMessage(val text: String = "", val author: String = "") {
    fun toMessage() = Message(text = text, author = author)

    companion object {
        fun validate(input: CreateMessage) = validation {
            if (input.author.isBlank()) {
                addError("Author is required")
            }
            if (input.text.isBlank()) {
                addError("Text is required")
            }
        }
    }
}
