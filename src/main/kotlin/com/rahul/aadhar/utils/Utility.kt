package com.rahul.aadhar.utils

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate
import java.text.SimpleDateFormat
import java.util.*

val otpTimestampFormatter = SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss")

val txnIdDateFormat = SimpleDateFormat("yyyyMMddHHmmssSSS")

fun getTimeStampForOtpRequest() = otpTimestampFormatter.format(Date())

fun getOtpTxnId(aua: String): String {
    return "AuthDemoClient" + ":" + aua + ":" + txnIdDateFormat.format(Date())
}

fun postSignedRequest(signedRequest: String, restTemplate: RestTemplate, url: String): String {
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_XML
    val entity = HttpEntity(signedRequest, headers)
    return restTemplate.postForEntity(url, entity, String::class.java).body!!
}