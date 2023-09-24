package io.github.wulkanowy.schools.integrity

import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.playintegrity.v1.PlayIntegrity
import com.google.api.services.playintegrity.v1.model.DecodeIntegrityTokenRequest
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import com.google.common.collect.Lists
import kotlinx.serialization.json.Json
import java.util.logging.Logger

fun decryptToken(tokenString: String, playIntegrity: PlayIntegrity = getPlayIntegrity()): IntegrityVerdictPayload {
    val decodeTokenRequest = DecodeIntegrityTokenRequest().setIntegrityToken(tokenString)
    val returnString = playIntegrity.v1()
        .decodeIntegrityToken(APPLICATION_PACKAGE_IDENTIFIER, decodeTokenRequest)
        .execute()
        .toPrettyString()

    val log = Logger.getLogger("decryptToken")
    log.info("Decrypted token: $returnString")

    return Json.decodeFromString(returnString)
}

fun getPlayIntegrity(): PlayIntegrity {
    val googleCredentials = GoogleCredentials.getApplicationDefault()
        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/playintegrity"))
    return PlayIntegrity.Builder(
        NetHttpTransport(),
        GsonFactory.getDefaultInstance(),
        HttpCredentialsAdapter(googleCredentials)
    )
        .setApplicationName("application")
        .build()
}
