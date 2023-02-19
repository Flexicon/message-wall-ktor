package com.flexicondev.messagewall.web

import io.ktor.server.plugins.requestvalidation.ValidationResult

class ValidationBuilder {
    private val errors = mutableListOf<String>()

    fun addError(message: String) {
        errors.add(message)
    }

    fun build() = errors.let {
        if (it.isEmpty()) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(it)
        }
    }
}

inline fun validation(build: ValidationBuilder.() -> Unit) =
    ValidationBuilder().apply(build).build()
