package io.github.wulkanowy.schools.plugins

import io.github.wulkanowy.schools.dao.LoginEventDao
import io.github.wulkanowy.schools.integrity.*
import io.github.wulkanowy.schools.model.ListResponse
import io.github.wulkanowy.schools.model.LoginEvent
import io.github.wulkanowy.schools.model.LoginEvents
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SortOrder
import java.util.logging.Level
import java.util.logging.Logger

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
                    Logger.getLogger("result").log(Level.INFO, "${request.data.uuid}: $result")
                    loginEventDao.addLoginEvent(request.data)
                    call.respond(status = HttpStatusCode.NoContent, "")
                }

                ValidateResult.VALIDATE_NONCE_NOT_FOUND,
                ValidateResult.VALIDATE_NONCE_MISMATCH,
                ValidateResult.VALIDATE_INTEGRITY_FAIL -> {
                    Logger.getLogger("result").log(Level.INFO, "${request.data.uuid}: $result")
                    call.respond(status = HttpStatusCode.BadRequest, message = result.name)
                }
            }
        }
        get("/log/list") {
            val params = call.request.queryParameters
            call.respond(
                ListResponse(
                    rows = loginEventDao.allLoginEvents(
                        page = params["page"]?.toLongOrNull() ?: 0,
                        pageSize = params["pageSize"]?.toIntOrNull() ?: 10,
                        text = params["text"],
                        orderBy = when (params["sortBy"]) {
                            "id" -> LoginEvents.id
                            "schoolName" -> LoginEvents.schoolName
                            "schoolShort" -> LoginEvents.schoolShort
                            "schoolAddress" -> LoginEvents.schoolAddress
                            else -> null
                        },
                        order = when (params["order"]) {
                            "asc" -> SortOrder.ASC
                            "desc" -> SortOrder.DESC
                            else -> null
                        },
                    ),
                    rowsCount = loginEventDao.getCount(text = params["text"]),
                )
            )
        }
        singlePageApplication {
            useResources = true
            filesPath = "app"
            defaultPage = "index.html"
        }
    }
}
