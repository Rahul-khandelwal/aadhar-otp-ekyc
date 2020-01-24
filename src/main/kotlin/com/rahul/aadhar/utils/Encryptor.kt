package com.rahul.aadhar.utils

import com.rahul.aadhar.config.ApplicationProperties
import org.bouncycastle.crypto.InvalidCipherTextException
import org.bouncycastle.crypto.engines.AESEngine
import org.bouncycastle.crypto.paddings.PKCS7Padding
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher
import org.bouncycastle.crypto.params.KeyParameter
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.beans.factory.annotation.Autowired
import java.io.FileInputStream
import java.io.IOException
import java.security.*
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.text.SimpleDateFormat
import java.util.*
import javax.annotation.PostConstruct
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey


class Encryptor {

    @Autowired
    private lateinit var applicationProperties: ApplicationProperties

    private val JCE_PROVIDER = "BC"

    private val ASYMMETRIC_ALGO = "RSA/ECB/PKCS1Padding"
    private val SYMMETRIC_KEY_SIZE = 256

    private val CERTIFICATE_TYPE = "X.509"

    private var publicKey: PublicKey? = null
    private var certExpiryDate: Date? = null

    @PostConstruct
    fun init() {
        Security.addProvider(BouncyCastleProvider())

        val filePath = "${System.getProperty("user.dir")}/${applicationProperties.publicKeyFile}"

        FileInputStream(filePath).use {
            val certFactory = CertificateFactory.getInstance(CERTIFICATE_TYPE, JCE_PROVIDER)
            val cert = certFactory.generateCertificate(it) as X509Certificate
            publicKey = cert.publicKey
            certExpiryDate = cert.notAfter
        }
    }

    /**
     * Creates an AES key which can be used as session key (sKey)
     *
     * @return
     *
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    @Throws(NoSuchAlgorithmException::class, NoSuchProviderException::class)
    fun generateSessionKey(): ByteArray {
        val keyGen = KeyGenerator.getInstance("AES", JCE_PROVIDER)
        keyGen.init(SYMMETRIC_KEY_SIZE)
        val key: SecretKey = keyGen.generateKey()
        return key.encoded!!
    }

    /**
     * Encrypts session key using UIDAI public key
     *
     * @param sessionKey session key to encrypt
     *
     * @return Encrypted data
     *
     * @throws IOException
     * @throws GeneralSecurityException
     */
    @Throws(IOException::class, GeneralSecurityException::class)
    fun encryptSessionKeyUsingPublicKey(sessionKey: ByteArray): ByteArray {
        // encrypt the session key with the public key
        val pkCipher = Cipher.getInstance(ASYMMETRIC_ALGO, JCE_PROVIDER)
        pkCipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return pkCipher.doFinal(sessionKey)
    }

    /**
     * Encrypts given data using session key
     *
     * @param sKey Session key
     * @param data Data to encrypt
     *
     * @return Encrypted data
     *
     * @throws InvalidCipherTextException
     */
    @Throws(InvalidCipherTextException::class)
    fun encryptDataUsingSessionKey(sKey: ByteArray?, data: ByteArray): ByteArray {
        val cipher = PaddedBufferedBlockCipher(AESEngine(), PKCS7Padding())
        cipher.init(true, KeyParameter(sKey))
        val outputSize = cipher.getOutputSize(data.size)
        val tempOP = ByteArray(outputSize)
        val processLen = cipher.processBytes(data, 0, data.size, tempOP, 0)
        val outputLen = cipher.doFinal(tempOP, processLen)
        val result = ByteArray(processLen + outputLen)
        System.arraycopy(tempOP, 0, result, 0, result.size)
        return result
    }

    /**
     * Returns UIDAI certificate's expiry date in YYYYMMDD format using GMT time zone.
     * It can be used as certificate identifier.
     *
     * @return Certificate identifier for UIDAI public certificate
     */
    fun getCertificateIdentifier(): String {
        val ciDateFormat = SimpleDateFormat("yyyyMMdd")
        ciDateFormat.timeZone = TimeZone.getTimeZone("GMT")
        return ciDateFormat.format(certExpiryDate)
    }
}