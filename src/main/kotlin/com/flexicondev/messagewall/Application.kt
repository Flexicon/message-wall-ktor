package com.flexicondev.messagewall

import com.flexicondev.messagewall.plugins.configureHTTP
import com.flexicondev.messagewall.plugins.configureMonitoring
import com.flexicondev.messagewall.plugins.configureRouting
import com.flexicondev.messagewall.plugins.configureSerialization
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
        configureHTTP()
        configureMonitoring()
        configureSerialization()
        configureRouting()
    }.start(wait = true)
}
