package com.rahul.aadhar.otp._2_5

import com.rahul.aadhar.utils.getTimeStampForOtpRequest

class Otp(val UID: String, val auaCode: String, val asaCode: String, val txnId: String, val auaLicenseKey: String) {
    val timeStamp = getTimeStampForOtpRequest()
    val version = "2.5"
    val type = "A"

    fun toXmlRequest() : String {
        /**
         * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
         * <Otp uid="" ts="" ac="" sa="" ver="" txn="" lk="" type=""></Otp>
         */

        return """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <Otp uid="${UID}" ts="${timeStamp}" ac="${auaCode}" sa="${asaCode}" ver="${version}" txn="${txnId}" lk="${auaLicenseKey}" type="${type}">
            </Otp>
        """.trimIndent()
    }
}