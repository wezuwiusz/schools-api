package io.github.wulkanowy.schools.plugins

import io.github.wulkanowy.schools.model.LoginEvent
import io.github.wulkanowy.schools.module
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class RoutingKtTest {

    @Test
    fun testPostLogLoginevent() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("http://localhost/log/loginEvent") {
            contentType(ContentType.Application.Json)
            setBody(
                LoginEvent(
                    schoolName = "Publiczna szkoła Wulkanowego nr 1 w fakelog.cf",
                    schoolAddress = "Ul. Wulkanowego 30, 30-300 Fakelog.cf, Polska",
                    scraperBaseUrl = "https://fakelog.cf",
                    symbol = "powiatwulkanowy",
                    loginType = "STANDARD",
                    uuid = UUID.randomUUID().toString(),
                    schoolId = "",
                    schoolShort = ""
                )
            )
        }
        assertEquals(HttpStatusCode.NoContent, response.status)
    }
}
