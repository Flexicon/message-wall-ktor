package com.flexicondev.messagewall.web

import com.flexicondev.messagewall.domain.Message
import com.flexicondev.messagewall.domain.MessageRepository
import com.flexicondev.messagewall.web.requests.CreateMessage
import com.flexicondev.messagewall.web.responses.MessageResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.joda.time.DateTimeZone

fun Routing.messageRoutes(repository: MessageRepository) {
    route("/messages") {
        get {
            call.respond(repository.findAllDescending().map { it.toResponse() })
        }

        post {
            val payload = call.receive<CreateMessage>()
            val message = repository.save(payload.toMessage())
            call.respond(HttpStatusCode.Created, message.toResponse())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
            val message = repository.findBy(id) ?: throw NoSuchElementException("Message with id $id not found")

            call.respond(message.toResponse())
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)

            repository.deleteBy(id)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}

fun Message.toResponse() = MessageResponse(
    id.toString(),
    text,
    author,
    createdAt.toDateTime(DateTimeZone.UTC).toInstant()
)
