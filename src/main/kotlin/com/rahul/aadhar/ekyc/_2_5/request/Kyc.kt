package com.rahul.aadhar.ekyc._2_5.request

import com.rahul.aadhar.auth._2_5.request.Auth
import java.util.*

/**
 * Format -
 * <Kyc ver="" ra="" rc="" lr="" de="" pfr="">
 *  <Rad>base64 encoded fully valid Auth XML for resident</Rad>
 * </Kyc>
 *
 * 'lr' attribute is for local language which is 'N' by default as desired.
 */
class Kyc(private val auth: Auth) {

    private val version = "2.5"
    private val ra = "O" // For OTP authentication type
    private val rc = "Y" // Consent
    private val de = "Y" // encrypt using asa public key, asa public key will be used for decryption
    private val pfr = "Y" // Print aadhar in response
    private val rad = Base64.getEncoder().encode(auth.toXmlRequest().toByteArray())

    fun toXmlRequest() : String {
        return """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <Kyc ver="$version" ra="$ra" rc="$rc" de="$de" pfr="$pfr">
                <Rad>$rad</Rad>
            </Kyc>
        """.trimIndent()
    }
}