package com.flexicondev.messagewall.plugins

import com.flexicondev.messagewall.routes.rootRoute
import com.flexicondev.messagewall.routes.messageRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        rootRoute()
        messageRoutes()
    }
}
