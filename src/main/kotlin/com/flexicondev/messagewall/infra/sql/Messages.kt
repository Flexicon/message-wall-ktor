package com.flexicondev.messagewall.infra.sql

import org.jetbrains.exposed.sql.Table

object Messages : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val text = text("name")
    val author = varchar("id", 255)
    val createdAt = datetime("created_at")
}
