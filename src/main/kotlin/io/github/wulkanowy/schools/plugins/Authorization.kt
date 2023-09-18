package io.github.wulkanowy.schools.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureAuthorization() {
    authentication {
        bearer("auth") {
            realm = "Access to the '/log' path"

            authenticate { tokenCredential ->
                if (tokenCredential.token == System.getenv("TOKEN")) {
                    UserIdPrincipal("wulkanowy-app-play")
                } else {
                    null
                }
            }
        }
    }
}
