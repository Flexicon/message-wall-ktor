package com.flexicondev.messagewall.web.responses

import kotlinx.serialization.Serializable

@Serializable
data class ApiError<T>(val status: Int, val title: String, val detail: T? = null)
