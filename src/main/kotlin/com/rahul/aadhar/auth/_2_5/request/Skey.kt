package com.rahul.aadhar.auth._2_5.request

/**
 * Format -
 * <Skey ci="">encrypted and encoded session key</Skey>
 */
class Skey(private val value: ByteArray, private val ci: String) {

    fun toXmlRequest() : String {
        return """
            <Skey ci="$ci">${value}</Skey>
        """.trimIndent()
    }
}
