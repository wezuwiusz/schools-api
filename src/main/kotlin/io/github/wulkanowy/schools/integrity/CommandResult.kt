package io.github.wulkanowy.schools.integrity

import kotlinx.serialization.Serializable

@Serializable
data class CommandResult(
    val commandSuccess: Boolean,
    val diagnosticMessage: String,
)
