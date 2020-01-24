package com.rahul.aadhar.auth._2_5.request

/**
 * Format -
 * <Data type="X|P">encrypted PID block</Data>
 *
 * Type is optional for X content, so we'll exclude that
 */
class Data(private val encryptedPid: ByteArray) {

    fun toXmlRequest() : String {
        return """
            <Data>${encryptedPid}</Data>
        """.trimIndent()
    }
}