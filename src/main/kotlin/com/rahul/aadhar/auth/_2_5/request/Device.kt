package com.rahul.aadhar.auth._2_5.request

/**
 * Format -
 * <Device rdsId="" rdsVer="" dpId="" dc="" mi="" mc="" />
 *
 * This is required for only biometric authentication
 */
class Device {

    fun toXmlRequest() : String {
        // For now return hardcoded XML

        return """
            <Device rdsId="" rdsVer="" dpId="" dc="" mi="" mc="" />
        """.trimIndent()
    }
}