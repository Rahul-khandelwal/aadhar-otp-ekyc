package com.rahul.aadhar.otp._2_5.request

import com.rahul.aadhar.utils.getTimeStampForOtpRequest

class Otp(private val UID: String, private val auaCode: String, private val asaCode: String,
          private val txnId: String, val auaLicenseKey: String) {
    private val timeStamp = getTimeStampForOtpRequest()
    private val version = "2.5"
    private val type = "A"

    fun toXmlRequest() : String {
        /**
         * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
         * <Otp uid="" ts="" ac="" sa="" ver="" txn="" lk="" type=""></Otp>
         */

        return """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <Otp uid="$UID" ts="$timeStamp" ac="$auaCode" sa="$asaCode" ver="$version" txn="$txnId" lk="$auaLicenseKey" type="$type">
            </Otp>
        """.trimIndent()
    }
}