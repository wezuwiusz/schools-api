package io.github.wulkanowy.schools.integrity

import kotlinx.serialization.Serializable

@Serializable
data class ServerCommand(
    val commandString: String,
    val tokenString: String
)
