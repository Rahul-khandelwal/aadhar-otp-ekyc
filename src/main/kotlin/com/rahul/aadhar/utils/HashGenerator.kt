package com.rahul.aadhar.utils

import java.security.MessageDigest

object HashGenerator {

    fun generateSha256Hash(message: ByteArray?): ByteArray? {
        val algorithm = "SHA-256"
        val securityProvider = "BC"
        var hash: ByteArray? = null
        if (message != null) {
            try {
                val digest = MessageDigest.getInstance(algorithm, securityProvider)
                digest.reset()
                hash = digest.digest(message)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return hash
    }

}