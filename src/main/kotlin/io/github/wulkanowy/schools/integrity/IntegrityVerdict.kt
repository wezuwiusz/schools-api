package io.github.wulkanowy.schools.integrity

import kotlinx.serialization.Serializable

@Serializable
data class RequestDetails(
    val requestPackageName: String? = null,
    val timestampMillis: Long = 0,
    val nonce: String? = null
)

@Serializable
data class AppIntegrity(
    val appRecognitionVerdict: String? = null,
    val packageName: String? = null,
    val certificateSha256Digest: List<String>? = null,
    val versionCode: Int = 0
)

@Serializable
data class DeviceIntegrity(
    val deviceRecognitionVerdict: List<String>? = null
)

@Serializable
data class AccountDetails(
    val appLicensingVerdict: String? = null
)

@Serializable
data class IntegrityVerdict(
    val requestDetails: RequestDetails,
    val appIntegrity: AppIntegrity,
    val deviceIntegrity: DeviceIntegrity,
    val accountDetails: AccountDetails
)

@Serializable
data class IntegrityVerdictPayload(
    val tokenPayloadExternal: IntegrityVerdict
)
