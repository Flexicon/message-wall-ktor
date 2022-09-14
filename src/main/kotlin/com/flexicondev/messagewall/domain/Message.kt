package com.flexicondev.messagewall.domain

import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime

data class Message(
    val id: Int? = null,
    val text: String,
    val author: String,
    val createdAt: LocalDateTime = LocalDateTime.now(DateTimeZone.UTC),
)
