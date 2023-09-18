package io.github.wulkanowy.schools

import io.github.wulkanowy.schools.model.LoginEvents
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val database = Database.connect(
            url = "jdbc:pgsql://localhost:5004/schools",
            driver = "com.impossibl.postgres.jdbc.PGDriver",
            user = "postgres",
            password = "postgres",
        )

        transaction(database) {
            SchemaUtils.create(LoginEvents)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
