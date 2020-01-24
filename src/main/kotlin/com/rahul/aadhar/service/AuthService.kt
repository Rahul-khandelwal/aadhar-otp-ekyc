package com.rahul.aadhar.service

import com.rahul.aadhar.auth._2_5.request.Auth
import com.rahul.aadhar.auth._2_5.request.Pid
import com.rahul.aadhar.config.ApplicationProperties
import com.rahul.aadhar.utils.DigitalSigner
import com.rahul.aadhar.utils.Encryptor
import com.rahul.aadhar.utils.getOtpTxnId
import com.rahul.aadhar.utils.postSignedRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class AuthService {

    @Autowired
    private lateinit var digitalSigner: DigitalSigner

    @Autowired
    private lateinit var encryptor: Encryptor

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Autowired
    private lateinit var applicationProperties: ApplicationProperties

    fun authenticate(uid: String, otp: String) : String {
        val pidRequest = Pid(otp = otp)

        val authRequest = Auth(uid, applicationProperties.auaCode, applicationProperties.asaCode,
                getOtpTxnId(applicationProperties.auaCode), applicationProperties.auaLicenseKey, encryptor, pidRequest)

        val uriString = applicationProperties.authServerUrl + "/" + applicationProperties.auaCode +
                "/" + uid[0] + "/" + uid[1] + "/" + applicationProperties.asaLicenseKey

        val signedAuthRequest = digitalSigner.signXML(authRequest.toXmlRequest(), true)
        return postSignedRequest(signedAuthRequest, restTemplate, uriString)
    }
}