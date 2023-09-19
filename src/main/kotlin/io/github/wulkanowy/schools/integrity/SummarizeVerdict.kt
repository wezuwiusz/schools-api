package io.github.wulkanowy.schools.integrity

// Build a summary string of integrity verdict information. This is used for
// informational purposes, you would not normally pass this information back
// to the client.
fun summarizeVerdict(integrityVerdict: IntegrityVerdict): String {
    var verdictString = "Device integrity: "
    var foundDeviceIntegritySignal = false
    for (deviceField in integrityVerdict.deviceIntegrity.deviceRecognitionVerdict!!) {
        when (deviceField) {
            VERDICT_VAL_MEETS_BASIC_INTEGRITY -> {
                foundDeviceIntegritySignal = true
                verdictString += "Basic "
            }

            VERDICT_VAL_MEETS_DEVICE_INTEGRITY -> {
                foundDeviceIntegritySignal = true
                verdictString += "Device "
            }

            VERDICT_VAL_MEETS_STRONG_INTEGRITY -> {
                foundDeviceIntegritySignal = true
                verdictString += "Strong "
            }

            VERDICT_VAL_MEETS_VIRTUAL_INTEGRITY -> {
                foundDeviceIntegritySignal = true
                verdictString += "Virtual "
            }
        }
    }
    if (!foundDeviceIntegritySignal) {
        verdictString = "Not found"
    }

    verdictString += when (integrityVerdict.appIntegrity.appRecognitionVerdict) {
        VERDICT_VAL_VERSION_RECOGNIZED -> "\nApp version recognized"
        VERDICT_VAL_VERSION_UNRECOGNIZED -> "\nApp version unrecognized"
        else -> "\nApp version unevaluated"
    }

    verdictString += when (integrityVerdict.accountDetails.appLicensingVerdict) {
        VERDICT_VAL_LICENSED -> "\nApp licensed"
        VERDICT_VAL_UNLICENSED -> "\nApp unlicensed"
        else -> "\nApp license unevaluated"
    }

    verdictString += when (integrityVerdict.requestDetails.requestPackageName) {
        APPLICATION_PACKAGE_IDENTIFIER -> "\nPackage name match"
        else -> "\nPackage name mismatch"
    }

    return verdictString
}
