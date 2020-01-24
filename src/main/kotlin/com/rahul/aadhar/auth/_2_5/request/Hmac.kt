package com.rahul.aadhar.auth._2_5.request

/**
 * Format -
 * <Hmac>SHA-256 Hash of Pid block, encrypted and then encoded</Hmac>
 *
 * To get Hmac Data -
 * After forming Pid XML, compute SHA-256 hash of Pid XML string
 * Then encrypt using session key
 * Then encode using base-64 encoding
 */
class Hmac(private val encodedHmacData: ByteArray) {

    fun toXmlRequest() : String {
        return """
            <Hmac>${encodedHmacData}</Hmac>
        """.trimIndent()
    }
}