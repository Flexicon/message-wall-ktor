package com.flexicondev.messagewall.plugins

import com.flexicondev.messagewall.web.requests.CreateMessage
import com.flexicondev.messagewall.web.responses.ApiError
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

fun Application.configureHTTP() {
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
    }

    install(RequestValidation) {
        validate(CreateMessage::validate)
    }

    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            HttpStatusCode.BadRequest.also {
                call.respond(it, ApiError(it.value, it.description, cause.reasons.joinToString(", ")))
            }
        }

        exception<NoSuchElementException> { call, cause ->
            HttpStatusCode.NotFound.also {
                call.respond(it, ApiError(it.value, it.description, cause.message))
            }
        }
    }
}
