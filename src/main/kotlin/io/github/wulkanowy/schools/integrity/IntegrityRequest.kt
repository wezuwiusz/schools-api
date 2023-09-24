package io.github.wulkanowy.schools.integrity

import kotlinx.serialization.Serializable

@Serializable
data class IntegrityRequest<T>(
    val tokenString: String,
    val data: T,
)
