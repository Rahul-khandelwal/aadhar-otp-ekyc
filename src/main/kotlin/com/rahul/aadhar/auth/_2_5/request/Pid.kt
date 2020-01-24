package com.rahul.aadhar.auth._2_5.request

import com.rahul.aadhar.utils.HashGenerator
import com.rahul.aadhar.utils.getTimeStampForOtpRequest

/**
 * Format -
 * <Pid ts="" ver="" wadh=””>
 *   <Demo lang="">
 *      <Pi ms="E" mv="" name="" lname="" lmv="" gender="M|F|T" dob="" dobt="V|D|A" age="" phone="" email=""/>
 *      <Pa ms="E" co="" house="" street="" lm="" loc="" vtc="" subdist="" dist="" state="" country="" pc="" po=""/>
 *      <Pfa ms="E" mv="" av="" lav="" lmv=""/>
 *   </Demo>
 *   <Bios dih="">
 *      <Bio type="FMR|FIR|IIR|FID" posh="" bs="">encoded biometric</Bio>
 *   </Bios>
 *   <Pv otp="" pin=""/>
 * </Pid>
 *
 * 'wadh' is wrapper api data hash, it will be only used for e-kyc.
 *
 * 'Demo' element is optional and describes personal information and
 * matching strategy based on that, we'll exclude it.
 *
 * 'Bios' element is also optional and required for biometrics authentication only.
 *
 * 'Pv' element is also optional and used for PIN/OTP, we'll use this.
 */
class Pid(private val wadh: String = "", private val otp: String) {

    private val timeStamp = getTimeStampForOtpRequest()
    private val version = "2.0"

    fun toXmlRequest() : String {
        return """
            <Pid ts="$timeStamp" ver="$version" wadh=”$wadh”>
                <Pv otp="$otp"/>
            </Pid>
        """.trimIndent()
    }

    fun getSha256HashOfXmlRequest() : ByteArray {
        return HashGenerator.generateSha256Hash(toXmlRequest().toByteArray())!!
    }
}