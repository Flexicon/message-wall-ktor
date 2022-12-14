package com.flexicondev.messagewall

import com.flexicondev.messagewall.infra.postgres.DatabaseFactory
import com.flexicondev.messagewall.plugins.configureHTTP
import com.flexicondev.messagewall.plugins.configureMonitoring
import com.flexicondev.messagewall.plugins.configureRouting
import com.flexicondev.messagewall.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.cio.EngineMain

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    DatabaseFactory.init(environment.config)

    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureRouting(DatabaseFactory.messageRepository)
}
