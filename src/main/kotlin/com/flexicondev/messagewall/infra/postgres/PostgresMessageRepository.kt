package com.flexicondev.messagewall.infra.postgres

import com.flexicondev.messagewall.domain.Message
import com.flexicondev.messagewall.domain.MessageRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class PostgresMessageRepository : MessageRepository {
    override fun findAllDescending(): Collection<Message> = transaction {
        Messages.selectAll()
            .orderBy(Messages.createdAt, SortOrder.DESC)
            .map { it.toMessage() }
    }

    override fun findBy(id: Int): Message? = transaction {
        Messages.select { Messages.id eq id }.singleOrNull()?.toMessage()
    }

    override fun save(message: Message) = if (message.id == null) {
        create(message)
    } else {
        update(message)
    }

    override fun deleteBy(id: Int) {
        transaction { Messages.deleteWhere { Messages.id eq id } }
    }

    override fun deleteAll() {
        transaction { Messages.deleteAll() }
    }

    private fun create(message: Message): Message {
        val id = transaction {
            Messages.insert {
                it[text] = message.text
                it[author] = message.author
                it[createdAt] = message.createdAt.toDateTime()
            } get Messages.id
        }

        return message.withId(id)
    }

    private fun update(message: Message): Message {
        transaction {
            Messages.update({ Messages.id eq message.id!! }) {
                it[text] = message.text
                it[author] = message.author
                it[createdAt] = message.createdAt.toDateTime()
            }
        }

        return message
    }

    private fun ResultRow.toMessage(): Message = Message(
        this[Messages.id],
        this[Messages.text],
        this[Messages.author],
        this[Messages.createdAt].toLocalDateTime()
    )

    private fun Message.withId(id: Int) = Message(id, text, author, createdAt)
}
