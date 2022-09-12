package com.flexicondev.messagewall.domain

import java.time.LocalDateTime
import java.time.ZoneOffset

data class Message(
    val id: Int? = null,
    val text: String,
    val author: String,
    val createdAt: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
)
