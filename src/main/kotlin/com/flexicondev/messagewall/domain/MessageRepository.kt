package com.flexicondev.messagewall.domain

interface MessageRepository {
    fun findAllDescending(): Collection<Message>

    fun findBy(id: Int): Message?

    fun save(message: Message): Message

    fun deleteBy(id: Int)

    fun deleteAll()
}
