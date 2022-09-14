package com.flexicondev.messagewall

import com.flexicondev.messagewall.domain.Message
import com.flexicondev.messagewall.domain.MessageRepository
import com.flexicondev.messagewall.plugins.configureHTTP
import com.flexicondev.messagewall.plugins.configureRouting
import com.flexicondev.messagewall.plugins.configureSerialization
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.testApplication
import org.joda.time.DateTimeZone
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        environment {
            config = MapApplicationConfig("ktor.environment" to "test")
        }
        application {
            configureRouting(mock())
            configureSerialization()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""{"message":"message-wall api"}""", bodyAsText())
        }
    }

    @Test
    fun testMessagesList() = testApplication {
        environment {
            config = MapApplicationConfig("ktor.environment" to "test")
        }
        application {
            configureRouting(mock())
            configureSerialization()
        }

        client.get("/messages").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""[]""", bodyAsText())
        }
    }

    @Test
    fun testMessageShowNotFound() = testApplication {
        environment {
            config = MapApplicationConfig("ktor.environment" to "test")
        }
        application {
            configureHTTP()
            configureRouting(mock())
            configureSerialization()
        }

        client.get("/messages/1").apply {
            assertEquals(HttpStatusCode.NotFound, status)
            assertEquals("Message with id 1 not found", bodyAsText())
        }
    }

    @Test
    fun testMessageShow() = testApplication {
        val id = 1
        val message = Message(id, "Avada Kedavra", "Voldemort")
        val messageTimestamp = message.createdAt.toDateTime(DateTimeZone.UTC).toInstant()
        val repositoryMock = mock<MessageRepository> {
            on { findBy(id) } doReturn message
        }

        environment {
            config = MapApplicationConfig("ktor.environment" to "test")
        }
        application {
            configureHTTP()
            configureRouting(repositoryMock)
            configureSerialization()
        }

        client.get("/messages/1").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                """
                {"id":"$id","text":"${message.text}","author":"${message.author}","timestamp":"$messageTimestamp"}
                """.trimIndent(),
                bodyAsText()
            )
        }
    }
}
