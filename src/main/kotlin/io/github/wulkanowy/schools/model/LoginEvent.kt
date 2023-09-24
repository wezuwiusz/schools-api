package io.github.wulkanowy.schools.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.util.*

@Serializable
data class LoginEvent(
    val uuid: String,
    val schoolName: String,
    val schoolShort: String,
    val schoolAddress: String,
    val scraperBaseUrl: String,
    val symbol: String,
    val schoolId: String,
    val loginType: String,
)

object LoginEvents : Table() {
    val id = integer("id").autoIncrement()
    val uuid = varchar("uuid", 36)
    val timestamp = timestamp("timestamp")
    val schoolName = varchar("schoolName", 256)
    val schoolShort = varchar("schoolShortName", 256)
    val schoolAddress = varchar("schoolAddress", 256)
    val scraperBaseUrl = varchar("scraperBaseUrl", 128)
    val symbol = varchar("symbol", 64)
    val schoolId = varchar("schoolId", 16)
    val loginType = varchar("loginType", 32)

    override val primaryKey = PrimaryKey(id)

    init {
        uniqueIndex("Unique event constraint", uuid)
    }
}
