package io.github.wulkanowy.schools

import io.github.wulkanowy.schools.model.LoginEvents
import kotlinx.coroutines.Dispatchers
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val host = System.getenv("DB_HOST")
        val user = "mysql"
        val password = "mysql"
        val database = Database.connect(
            url = "jdbc:mysql://$host:3306/schools",
            driver = "com.mysql.cj.jdbc.Driver",
            user = user,
            password = password,
        )

        val flyway = Flyway.configure()
            .dataSource(database.url, user, password)
            .load()
        flyway.migrate()

        transaction(database) {
            SchemaUtils.statementsRequiredToActualizeScheme(LoginEvents).let {
                if (it.isNotEmpty()) {
                    println(it)
                    error("There is/are ${it.size} migrations to run!")
                }
            }
            SchemaUtils.create(LoginEvents)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
