package io.github.wulkanowy.schools.plugins

import io.github.wulkanowy.schools.dao.LoginEventDao
import io.github.wulkanowy.schools.model.LoginEvent
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val loginEventDao = LoginEventDao()

    routing {
        authenticate("auth") {
            post("/log/loginEvent") {
                val loginEvent = call.receive<LoginEvent>()
                loginEventDao.addLoginEvent(loginEvent)
                call.respond(status = HttpStatusCode.NoContent, "")
            }
        }
        get("/") {
            call.respond(loginEventDao.allLoginEvents())
        }
    }
}
