package io.github.wulkanowy.schools.integrity

import kotlinx.serialization.Serializable
import java.util.logging.Logger

val randomStorage = mutableListOf<IntegrityRandom>()

@Serializable
data class IntegrityRandom(val random: String, val timestamp: Long)
