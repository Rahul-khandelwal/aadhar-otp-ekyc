package com.rahul.aadhar.auth._2_5.request

/**
 * Auth Request format -
 *
 * <Auth uid="" rc="" tid="" ac="" sa="" ver="" txn="" lk="">
 *   <Uses pi="" pa="" pfa="" bio="" bt="" pin="" otp=""/>
 *   <Device rdsId="" rdsVer="" dpId="" dc="" mi="" mc="" />
 *   <Skey ci="">encrypted and encoded session key</Skey>
 *   <Hmac>SHA-256 Hash of Pid block, encrypted and then encoded</Hmac>
 *   <Data type="X|P">encrypted PID block</Data>
 *   <Signature>Digital signature of AUA</Signature>
 * </Auth>
 */
class Auth(private val UID: String, private val auaCode: String, private val asaCode: String,
           private val txnId: String, private val auaLicenseKey: String,
           private val encodedSessionKey: ByteArray) {

    private val rc = "Y" // User consent
    private val tid = "" // As we are not using biometrics
    private val version = "2.5"
    private val uses = Uses()
    private val device = Device()
    private val sKey = Skey(encodedSessionKey)
    private val hmac = Hmac("") // TODO
    private val data = Data("".toByteArray()) // TODO

    fun toXmlRequest() : String {
        return """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <Auth uid="$UID" rc="$rc" tid="$tid" ac="$auaCode" sa="$asaCode" ver="$version" txn="$txnId" lk="$auaLicenseKey">
                ${uses.toXmlRequest()}
                ${device.toXmlRequest()}
                ${sKey.toXmlRequest()}
                ${hmac.toXmlRequest()}
                ${data.toXmlRequest()}
            </Auth>
        """.trimIndent()
    }
}