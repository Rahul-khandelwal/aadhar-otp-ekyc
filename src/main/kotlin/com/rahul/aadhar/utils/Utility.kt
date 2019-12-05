package com.rahul.aadhar.utils

import java.text.SimpleDateFormat
import java.util.*

val otpTimestampFormatter = SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss")

val txnIdDateFormat = SimpleDateFormat("yyyyMMddHHmmssSSS")

fun getTimeStampForOtpRequest()  = otpTimestampFormatter.format(Date())

fun getOtpTxnId(aua: String): String {
    return "AuthDemoClient" + ":" + aua + ":" + txnIdDateFormat.format(Date())
}