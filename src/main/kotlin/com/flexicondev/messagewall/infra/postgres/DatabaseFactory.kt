package com.flexicondev.messagewall.infra.postgres

import com.flexicondev.messagewall.domain.MessageRepository
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.ApplicationConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(config: ApplicationConfig) {
        val driverClassName = config.property("ktor.database.driverClassName").getString()
        val jdbcURL = config.property("ktor.database.jdbcURL").getString()
        val username = config.property("ktor.database.user").getString()
        val password = config.property("ktor.database.password").getString()
        val maximumPoolSize = config.propertyOrNull("ktor.database.maximumPoolSize")?.getString()?.toInt()

        val hikariDS = HikariDataSource().apply {
            jdbcUrl = jdbcURL
            setUsername(username)
            setPassword(password)
            setDriverClassName(driverClassName)
            maximumPoolSize?.let { this.maximumPoolSize = it }
        }
        val database = Database.connect(hikariDS)

        transaction(database) {
            SchemaUtils.create(Messages)
        }
    }

    val messageRepository: MessageRepository
        get() = PostgresMessageRepository()
}
