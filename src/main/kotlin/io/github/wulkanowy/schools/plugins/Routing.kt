package io.github.wulkanowy.schools.plugins

import com.google.gson.Gson
import io.github.wulkanowy.schools.dao.LoginEventDao
import io.github.wulkanowy.schools.integrity.*
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

        route("/performCommand") {
            post {
                val incomingCommand = call.receive<ServerCommand>()
                val decodedTokenString = decryptToken(incomingCommand.tokenString)
                val integrityVerdictPayload = Gson()
                    .fromJson(decodedTokenString, IntegrityVerdictPayload::class.java)

                if (integrityVerdictPayload != null) {
                    val integrityVerdict = integrityVerdictPayload.tokenPayloadExternal
                    // Integrity signals didn't pass our 'success' criteria,
                    // pass the verdict summary string
                    // back in the diagnostic field
                    when (validateCommand(incomingCommand.commandString, integrityVerdict)) {
                        ValidateResult.VALIDATE_SUCCESS -> call.respond(
                            CommandResult(
                                commandSuccess = true,
                                diagnosticMessage = summarizeVerdict(integrityVerdict),
                            )
                        )

                        ValidateResult.VALIDATE_NONCE_NOT_FOUND -> call.respond(
                            CommandResult(
                                commandSuccess = false,
                                diagnosticMessage = "Failed to find matching nonce",
                            )
                        )

                        ValidateResult.VALIDATE_NONCE_EXPIRED -> call.respond(
                            CommandResult(
                                commandSuccess = false,
                                diagnosticMessage = "Token nonce expired",
                            )
                        )

                        ValidateResult.VALIDATE_NONCE_MISMATCH -> call.respond(
                            CommandResult(
                                commandSuccess = false,
                                diagnosticMessage = "Token nonce didn't match command hash",
                            )
                        )

                        ValidateResult.VALIDATE_INTEGRITY_FAIL -> call.respond(
                            CommandResult(
                                commandSuccess = false,
                                diagnosticMessage = summarizeVerdict(integrityVerdict),
                            )
                        )
                    }
                } else {
                    val invalidToken = CommandResult(
                        commandSuccess = false,
                        diagnosticMessage = "Token invalid",
                    )
                    call.respond(invalidToken)
                }
            }
        }
    }
}
