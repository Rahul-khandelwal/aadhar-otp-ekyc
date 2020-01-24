package com.rahul.aadhar.auth._2_5.request

/**
 * Format -
 * <Uses pi="" pa="" pfa="" bio="" bt="" pin="" otp=""/>
 *
 * In our case only OTP will be used, so other flags will have 'n' value
 */
class Uses {
    private val pi = "n"
    private val pa = "n"
    private val pfa = "n"
    private val bio = "n"
    private val bt = "" // Required only when bio attribute used
    private val pin = "n"
    private val otp = "y"

    fun toXmlRequest() : String {
        return """
            <Uses pi="$pi" pa="$pa" pfa="$pfa" bio="$bio" bt="$bt" pin="$pin" otp="$otp"/>
        """.trimIndent()
    }
}