package io.github.wulkanowy.schools.dao

import io.github.wulkanowy.schools.DatabaseFactory.dbQuery
import io.github.wulkanowy.schools.model.LoginEvent
import io.github.wulkanowy.schools.model.LoginEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class LoginEventDao {

    private fun resultRowToLoginEvent(row: ResultRow) = LoginEvent(
        schoolName = row[LoginEvents.schoolName],
        schoolAddress = row[LoginEvents.schoolAddress],
        scraperBaseUrl = row[LoginEvents.scraperBaseUrl],
        symbol = row[LoginEvents.symbol],
        loginType = row[LoginEvents.loginType],
        uuid = row[LoginEvents.uuid],
    )

    suspend fun allLoginEvents(): List<LoginEvent> = dbQuery {
        LoginEvents.selectAll().map(::resultRowToLoginEvent)
    }

    suspend fun addLoginEvent(event: LoginEvent) = withContext(Dispatchers.IO) {
        transaction {
            LoginEvents.insert {
                it[uuid] = event.uuid
                it[schoolName] = event.schoolName
                it[schoolAddress] = event.schoolAddress
                it[scraperBaseUrl] = event.scraperBaseUrl
                it[symbol] = event.symbol
                it[loginType] = event.loginType
                it[timestamp] = Instant.now()
            }
        }
    }
}
