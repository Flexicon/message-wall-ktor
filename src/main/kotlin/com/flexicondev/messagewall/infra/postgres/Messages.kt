package com.flexicondev.messagewall.infra.postgres

import org.jetbrains.exposed.sql.Table

object Messages : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val text = text("name")
    val author = varchar("author", 255)
    val createdAt = datetime("created_at")
}
