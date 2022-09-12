package com.flexicondev.messagewall.routes

import com.flexicondev.messagewall.domain.Message
import com.flexicondev.messagewall.http.responses.MessageResponse
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

val dummyMessage = MessageResponse.from(
    Message(1, "Hello there", "Obi-Wan Kenobi")
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
