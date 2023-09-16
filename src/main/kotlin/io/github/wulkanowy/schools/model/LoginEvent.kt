package io.github.wulkanowy.schools.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.timestamp

@Serializable
data class LoginEvent(
    val schoolName: String,
    val schoolAddress: String,
    val scraperBaseUrl: String,
    val symbol: String,
    val loginType: String,
)

object LoginEvents : Table() {
    val id = integer("id").autoIncrement()
    val timestamp = timestamp("timestamp")
    val schoolName = varchar("schoolName", 256)
    val schoolAddress = varchar("schoolAddress", 256)
    val scraperBaseUrl = varchar("scraperBaseUrl", 128)
    val symbol = varchar("symbol", 64)
    val loginType = varchar("loginType", 32)

    override val primaryKey = PrimaryKey(id)
}
