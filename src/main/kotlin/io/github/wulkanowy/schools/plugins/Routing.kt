package io.github.wulkanowy.schools.plugins

import io.github.wulkanowy.schools.dao.LoginEventDao
import io.github.wulkanowy.schools.integrity.*
import io.github.wulkanowy.schools.model.LoginEvent
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val loginEventDao = LoginEventDao()
    val playIntegrity = getPlayIntegrity()

    routing {
        post("/log/loginEvent") {
            val request = call.receive<IntegrityRequest<LoginEvent>>()
            val integrityVerdictPayload = decryptToken(request.tokenString, playIntegrity)
            val integrityVerdict = integrityVerdictPayload.tokenPayloadExternal

            when (val result = validateCommand(request.data.uuid, integrityVerdict)) {
                ValidateResult.VALIDATE_SUCCESS -> {
                    loginEventDao.addLoginEvent(request.data)
                    call.respond(status = HttpStatusCode.NoContent, "")
                }

                ValidateResult.VALIDATE_NONCE_NOT_FOUND,
                ValidateResult.VALIDATE_NONCE_MISMATCH,
                ValidateResult.VALIDATE_INTEGRITY_FAIL -> {
                    call.respond(status = HttpStatusCode.BadRequest, message = result.name)
                }
            }
        }
        get("/") {
            call.respond(loginEventDao.allLoginEvents())
        }
    }
}
