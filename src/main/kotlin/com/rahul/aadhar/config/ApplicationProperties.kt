package com.rahul.aadhar.config

import org.springframework.beans.factory.annotation.Value

class ApplicationProperties {

    @Value("\${signKeyStore}")
    lateinit var signKeyStore : String

    @Value("\${signaturePassword}") // keyStorePassword - Password of .p12 file
    lateinit var keyStorePassword: String

    @Value("\${signatureAlias}") // Alias of the certificate in .p12 file
    lateinit var keyStoreAlias: String

    @Value("\${publicKeyFile}") // keyStoreFile - Location of .p12 file
    lateinit var keyStoreFile: String

    @Value("\${auaLicenseKey}")
    lateinit var auaLicenseKey: String

    @Value("\${auaCode}")
    lateinit var auaCode: String

    @Value("\${asaLicenseKey}")
    lateinit var asaLicenseKey: String

    @Value("\${asaCode}")
    lateinit var asaCode: String

    @Value("\${otpServerUrl}")
    lateinit var otpServerUrl: String

    @Value("\${publicKeyFile}")
    lateinit var publicKeyFile: String
}