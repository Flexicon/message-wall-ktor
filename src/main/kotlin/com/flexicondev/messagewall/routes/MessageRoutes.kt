package com.flexicondev.messagewall.routes

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

val dummyMessage = mapOf(
    "id" to "1",
    "text" to "Hello there",
    "author" to "Obi-Wan Kenobi",
    "timestamp" to "022-07-12T20:51+0000",
)

fun Routing.messageRoutes() {
    // TODO: implement with proper models and DB/service calls
    route("/messages") {
        get {
            call.respond(listOf(dummyMessage))
        }

        post {
            call.respond(mapOf("method" to "post"))
        }

        get("/{id}") {
//            val id = call.parameters["id"]
            call.respond(dummyMessage)
        }

        delete("/{id}") {
            val id = call.parameters["id"]
            call.respond(mapOf("id" to id, "method" to "delete"))
        }
    }
}
