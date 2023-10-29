package io.github.wulkanowy.schools.dao

import io.github.wulkanowy.schools.DatabaseFactory.dbQuery
import io.github.wulkanowy.schools.model.LoginEvent
import io.github.wulkanowy.schools.model.LoginEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class LoginEventDao {

    private val uniqueColumns = listOf(
        LoginEvents.schoolId,
        LoginEvents.symbol,
        LoginEvents.scraperBaseUrl,
    )

    private fun resultRowToLoginEvent(row: ResultRow) = LoginEvent(
        schoolName = row[LoginEvents.schoolName],
        schoolShort = row[LoginEvents.schoolShort],
        schoolAddress = row[LoginEvents.schoolAddress],
        scraperBaseUrl = row[LoginEvents.scraperBaseUrl],
        symbol = row[LoginEvents.symbol],
        schoolId = row[LoginEvents.schoolId],
        loginType = row[LoginEvents.loginType],
        uuid = row[LoginEvents.uuid],
    )

    suspend fun allLoginEvents(
        page: Long,
        pageSize: Int,
        text: String?,
        orderBy: Column<*>?,
        order: SortOrder?,
    ): List<LoginEvent> = dbQuery {
        getQuery(text, orderBy, order)
            .limit(pageSize, page * pageSize)
            .map(::resultRowToLoginEvent)
    }

    suspend fun getCount(
        text: String?,
        orderBy: Column<*>?,
        order: SortOrder?,
    ) = dbQuery {
        val query = getQuery(text, orderBy, order).alias("getAll")
        query.selectAll().count()
    }

    private suspend fun getQuery(
        text: String?,
        orderBy: Column<*>?,
        order: SortOrder?,
    ) = dbQuery {
        val currentOrderBy = listOfNotNull(orderBy).toSet()
        val distinctColumns = currentOrderBy + (uniqueColumns - currentOrderBy)
        LoginEvents
            .slice(
                customDistinctOn(*distinctColumns.toTypedArray()),
                *(LoginEvents.columns).toTypedArray()
            )
            .selectAll()
            .let { query ->
                if (order != null) {
                    query.orderBy(*distinctColumns.map { it to order }.toTypedArray())
                } else query
            }
            .let {
                if (text.isNullOrBlank()) it else {
                    it.orWhere { LoginEvents.schoolName like "%${text}%" }
                        .orWhere { LoginEvents.schoolAddress like "%${text}%" }
                }
            }
    }

    suspend fun addLoginEvent(event: LoginEvent) = withContext(Dispatchers.IO) {
        transaction {
            LoginEvents.insertIgnore {
                it[uuid] = event.uuid
                it[schoolName] = event.schoolName
                it[schoolShort] = event.schoolShort
                it[schoolAddress] = event.schoolAddress
                it[scraperBaseUrl] = event.scraperBaseUrl
                it[symbol] = event.symbol
                it[schoolId] = event.schoolId
                it[loginType] = event.loginType
                it[timestamp] = Instant.now()
            }
        }
    }
}
