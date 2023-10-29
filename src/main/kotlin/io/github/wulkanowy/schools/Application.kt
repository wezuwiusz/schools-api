package io.github.wulkanowy.schools

import io.github.wulkanowy.schools.plugins.configureAuthorization
import io.github.wulkanowy.schools.plugins.configureRouting
import io.github.wulkanowy.schools.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(
        factory = Netty,
        port = 3002,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureAuthorization()
    configureRouting()
}
