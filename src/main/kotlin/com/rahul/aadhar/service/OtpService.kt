package com.rahul.aadhar.service

import com.rahul.aadhar.config.ApplicationProperties
import com.rahul.aadhar.utils.DigitalSigner
import com.rahul.aadhar.otp._2_5.request.Otp
import com.rahul.aadhar.utils.getOtpTxnId
import com.rahul.aadhar.utils.postSignedRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class OtpService {

    @Autowired
    private lateinit var digitalSigner: DigitalSigner

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Autowired
    private lateinit var applicationProperties: ApplicationProperties

    fun sendOtp(uid: String) : String {
        val otpRequest = Otp(uid, applicationProperties.auaCode, applicationProperties.asaCode,
                getOtpTxnId(applicationProperties.auaCode), applicationProperties.auaLicenseKey)

        val uriString = applicationProperties.otpServerUrl + "/" + applicationProperties.auaCode +
                "/" + uid[0] + "/" + uid[1] + "/" + applicationProperties.asaLicenseKey

        val signedOtpRequest = digitalSigner.signXML(otpRequest.toXmlRequest(), true)
        return postSignedRequest(signedOtpRequest, restTemplate, uriString)
    }
}