package com.flexicondev.messagewall.infra.sql

import com.flexicondev.messagewall.domain.Message
import com.flexicondev.messagewall.domain.MessageRepository

class SqlMessageRepository : MessageRepository {
    override fun findAllDescending(): Collection<Message> {
        TODO("Not yet implemented")
    }

    override fun findBy(id: Int): Message? {
        TODO("Not yet implemented")
    }

    override fun save(message: Message): Message {
        TODO("Not yet implemented")
    }

    override fun existsBy(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteBy(id: Int) {
        TODO("Not yet implemented")
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }
}
