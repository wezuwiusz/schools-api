package io.github.wulkanowy.schools.model

import kotlinx.serialization.Serializable

@Serializable
data class ListResponse(
    val rows: List<LoginEvent>,
    val rowsCount: Long,
)
