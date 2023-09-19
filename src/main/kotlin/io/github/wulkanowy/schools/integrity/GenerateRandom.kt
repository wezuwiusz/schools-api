package io.github.wulkanowy.schools.integrity

const val RANDOM_BYTE_COUNT = 16

fun ByteArray.toHexString(): String = joinToString(separator = "") {
        currentByte -> "%02x".format(currentByte) }

