package com.rahul.aadhar.utils

import java.security.MessageDigest

object HashGenerator {

    fun generateSha256Hash(message: String): ByteArray {
        return generateSha256Hash(message.toByteArray())
    }

    fun generateSha256Hash(message: ByteArray): ByteArray {
        val algorithm = "SHA-256"
        val securityProvider = "BC"
        val digest = MessageDigest.getInstance(algorithm, securityProvider)
        digest.reset()
        return digest.digest(message)
    }

}