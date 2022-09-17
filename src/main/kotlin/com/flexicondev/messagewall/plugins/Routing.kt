package com.flexicondev.messagewall.plugins

import com.flexicondev.messagewall.domain.MessageRepository
import com.flexicondev.messagewall.web.messageRoutes
import com.flexicondev.messagewall.web.rootRoute
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting(messageRepository: MessageRepository) {
    routing {
        rootRoute()
        messageRoutes(messageRepository)
    }
}
