package io.github.wulkanowy.schools.integrity

import java.util.logging.Logger

// Package name of the client application
const val APPLICATION_PACKAGE_IDENTIFIER = "io.github.wulkanowy"

// Values returned by the verdict that provide integrity signals
const val VERDICT_VAL_MEETS_BASIC_INTEGRITY = "MEETS_BASIC_INTEGRITY"
const val VERDICT_VAL_MEETS_DEVICE_INTEGRITY = "MEETS_DEVICE_INTEGRITY"
const val VERDICT_VAL_MEETS_STRONG_INTEGRITY = "MEETS_STRONG_INTEGRITY"
const val VERDICT_VAL_MEETS_VIRTUAL_INTEGRITY = "MEETS_VIRTUAL_INTEGRITY"
const val VERDICT_VAL_VERSION_UNRECOGNIZED = "UNRECOGNIZED_VERSION"
const val VERDICT_VAL_VERSION_RECOGNIZED = "PLAY_RECOGNIZED"
const val VERDICT_VAL_LICENSED = "LICENSED"

fun validateCommand(originalNonce: String, integrityVerdict: IntegrityVerdict): ValidateResult {
    if (integrityVerdict.requestDetails.nonce != null) {
        val decryptedNonce = integrityVerdict.requestDetails.nonce

        return if (originalNonce == decryptedNonce) {
            if (validateVerdict(integrityVerdict)) {
                ValidateResult.VALIDATE_SUCCESS
            } else {
                ValidateResult.VALIDATE_INTEGRITY_FAIL
            }
        } else {
            ValidateResult.VALIDATE_NONCE_MISMATCH
        }
    }
    return ValidateResult.VALIDATE_NONCE_NOT_FOUND
}

fun validateVerdict(integrityVerdict: IntegrityVerdict): Boolean {
    // Process the integrity verdict and 'validate' the command if the following positive
    // signals exist:
    // 1) Positive device integrity signal
    // 2) Recognized app version signal
    // 3) Licensed user signal
    // 4) Application package identifier match
    var metDeviceIntegrity = false
    for (deviceField in integrityVerdict.deviceIntegrity.deviceRecognitionVerdict!!) {
        when (deviceField) {
            VERDICT_VAL_MEETS_BASIC_INTEGRITY -> metDeviceIntegrity = true
            VERDICT_VAL_MEETS_DEVICE_INTEGRITY -> metDeviceIntegrity = true
            VERDICT_VAL_MEETS_STRONG_INTEGRITY -> metDeviceIntegrity = true
            VERDICT_VAL_MEETS_VIRTUAL_INTEGRITY -> metDeviceIntegrity = true
        }
    }

    if (metDeviceIntegrity) {
        val recognitionVerdict = integrityVerdict.appIntegrity.appRecognitionVerdict
        if (recognitionVerdict == VERDICT_VAL_VERSION_RECOGNIZED || recognitionVerdict == VERDICT_VAL_VERSION_UNRECOGNIZED) {
            if (integrityVerdict.accountDetails.appLicensingVerdict == VERDICT_VAL_LICENSED) {
                if (integrityVerdict.requestDetails.requestPackageName == APPLICATION_PACKAGE_IDENTIFIER) {
                    return true
                }
            }
        }
    }
    return false
}
