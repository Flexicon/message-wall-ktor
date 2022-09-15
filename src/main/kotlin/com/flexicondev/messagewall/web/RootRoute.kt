package com.flexicondev.messagewall.web

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get

fun Routing.rootRoute() {
    get("/") {
        call.respond(mapOf("message" to "message-wall api"))
    }
}
