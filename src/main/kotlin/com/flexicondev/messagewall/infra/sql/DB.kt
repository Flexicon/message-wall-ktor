package com.flexicondev.messagewall.infra.sql

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils

fun setupDB() {
    // TODO: connect to postgres later via config
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
    SchemaUtils.create(Messages)
}
