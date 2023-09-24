package io.github.wulkanowy.schools.integrity

// Return values for the validation result of a command with an associated
// Play Integrity token
enum class ValidateResult {
    VALIDATE_SUCCESS,
    VALIDATE_NONCE_NOT_FOUND,
    VALIDATE_NONCE_MISMATCH,
    VALIDATE_INTEGRITY_FAIL
}
