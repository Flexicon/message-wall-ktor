package com.flexicondev.messagewall

import com.flexicondev.messagewall.domain.Message
import com.flexicondev.messagewall.domain.MessageRepository
import com.flexicondev.messagewall.infra.postgres.DatabaseFactory
import com.flexicondev.messagewall.web.requests.CreateMessage
import com.flexicondev.messagewall.web.responses.MessageResponse
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import org.joda.time.DateTimeZone
import org.junit.Before
import org.junit.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@Testcontainers
class ApplicationTest {
    companion object {
        @Container
        private val postgreSQLContainer = PostgreSQLContainer("postgres:14.4-alpine")
    }

    private val messageRepository: MessageRepository

    init {
        postgreSQLContainer.start()
        DatabaseFactory.withTestInit().also {
            messageRepository = it.messageRepository
        }
    }

    @Before
    fun beforeEach() {
        messageRepository.deleteAll()
    }

    @Test
    fun testRoot() = testApplication {
        withTestConfig()

        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""{"message":"message-wall api"}""", bodyAsText())
        }
    }

    @Test
    fun testMessagesList() = testApplication {
        withTestConfig()

        client.get("/messages").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""[]""", bodyAsText())
        }
    }

    @Test
    fun testMessageShowNotFound() = testApplication {
        withTestConfig()

        client.get("/messages/1").apply {
            assertEquals(HttpStatusCode.NotFound, status)
            assertEquals("Message with id 1 not found", bodyAsText())
        }
    }

    @Test
    fun testMessageShow() = testApplication {
        withTestConfig()

        val message = DatabaseFactory.messageRepository.save(Message(text = "Avada Kedavra", author = "Voldemort"))
        val messageTimestamp = message.createdAt.toDateTime(DateTimeZone.UTC).toInstant()

        client.get("/messages/1").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                """
                {"id":"${message.id}","text":"${message.text}","author":"${message.author}","timestamp":"${messageTimestamp}"}
                """.trimIndent(),
                bodyAsText()
            )
        }
    }

    @Test
    fun testMessageCreate() = testApplication {
        withTestConfig()
        val payload = CreateMessage("Avada Kedavra", "Voldemort")

        val response = jsonClient.post("/messages") {
            contentType(ContentType.Application.Json)
            setBody(payload)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }

        val messageID = response.body<MessageResponse>().apply {
            assertNotNull(id)
            assertNotNull(timestamp)
            assertEquals(payload.text, text)
            assertEquals(payload.author, author)
        }.id

        jsonClient.get("/messages/$messageID").apply {
            assertEquals(HttpStatusCode.OK, status)
            with(body<MessageResponse>()) {
                assertEquals(messageID, id)
                assertEquals(payload.text, text)
                assertEquals(payload.author, author)
            }
        }
    }

    private fun ApplicationTestBuilder.withTestConfig() {
        environment {
            config = testConfig()
        }
        application {
            module()
        }
    }

    private fun testConfig() = MapApplicationConfig(
        mapOf(
            "ktor.database.driverClassName" to postgreSQLContainer.driverClassName,
            "ktor.database.jdbcURL" to postgreSQLContainer.jdbcUrl,
            "ktor.database.user" to postgreSQLContainer.username,
            "ktor.database.password" to postgreSQLContainer.password,
        ).toList()
    )

    private fun DatabaseFactory.withTestInit() = apply { init(testConfig()) }

    private val ApplicationTestBuilder.jsonClient
        get() = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
}
