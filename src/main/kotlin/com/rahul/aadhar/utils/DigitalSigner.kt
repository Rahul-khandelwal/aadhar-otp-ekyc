package com.rahul.aadhar.utils

import com.rahul.aadhar.config.ApplicationProperties
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.beans.factory.annotation.Autowired
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.FileInputStream
import java.io.IOException
import java.io.StringReader
import java.io.StringWriter
import java.security.KeyStore
import java.security.KeyStore.PasswordProtection
import java.security.Provider
import java.security.Security
import java.security.cert.X509Certificate
import java.util.*
import javax.annotation.PostConstruct
import javax.xml.crypto.dsig.*
import javax.xml.crypto.dsig.dom.DOMSignContext
import javax.xml.crypto.dsig.keyinfo.KeyInfo
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
import javax.xml.crypto.dsig.keyinfo.X509Data
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec
import javax.xml.crypto.dsig.spec.TransformParameterSpec
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class DigitalSigner {

    private val MEC_TYPE = "DOM"
    private val WHOLE_DOC_URI = ""
    private val KEY_STORE_TYPE = "PKCS12"

    private var provider: Provider? = null
    private var keyEntry: KeyStore.PrivateKeyEntry? = null

    @Autowired
    private lateinit var applicationProperties: ApplicationProperties

    @PostConstruct
    fun init() {
        keyEntry = getKeyFromKeyStore(applicationProperties.signKeyStore, applicationProperties.keyStorePassword.toCharArray(),
                applicationProperties.keyStoreAlias)

        if (keyEntry == null) {
            throw RuntimeException("Key could not be read for digital signature. Please check value of signature "
                    + "alias and signature password, and restart the Auth Client")
        }

        provider = BouncyCastleProvider()
        Security.addProvider(provider)
    }

    fun signXML(xmlDocument: String?, includeKeyInfo: Boolean): String? {
        return try {
            // Parse the input XML
            val dbf = DocumentBuilderFactory.newInstance()
            dbf.isNamespaceAware = true
            val inputDocument: Document = dbf.newDocumentBuilder().parse(
                    InputSource(StringReader(xmlDocument)))
            // Sign the input XML's DOM document
            val signedDocument: Document = sign(inputDocument, includeKeyInfo)
            // Convert the signedDocument to XML String
            val stringWriter = StringWriter()
            val tf = TransformerFactory.newInstance()
            val trans: Transformer = tf.newTransformer()
            trans.transform(DOMSource(signedDocument), StreamResult(
                    stringWriter))
            stringWriter.buffer.toString()
        } catch (e: Exception) {
            throw RuntimeException(
                    "Error while digitally signing the XML document", e)
        }
    }

    @Throws(Exception::class)
    private fun sign(xmlDoc: Document, includeKeyInfo: Boolean): Document {
        if (System.getenv("SKIP_DIGITAL_SIGNATURE") != null) {
            return xmlDoc
        }

        // Creating the XMLSignature factory.
        val fac: XMLSignatureFactory = XMLSignatureFactory.getInstance(MEC_TYPE)

        // Creating the reference object, reading the whole document for signing.
        val ref: Reference = fac.newReference(WHOLE_DOC_URI, fac.newDigestMethod(
                DigestMethod.SHA1, null), listOf(fac
                .newTransform(Transform.ENVELOPED,
                        null as TransformParameterSpec?)), null, null)

        // Create the SignedInfo.
        val sInfo: SignedInfo = fac
                .newSignedInfo(fac.newCanonicalizationMethod(
                        CanonicalizationMethod.INCLUSIVE,
                        null as C14NMethodParameterSpec?), fac
                        .newSignatureMethod(SignatureMethod.RSA_SHA1, null), listOf(ref))

        if (keyEntry == null) {
            throw RuntimeException(
                    "Key could not be read for digital signature. Please check value of signature alias and signature password, and restart the Auth Client")
        }

        val x509Cert: X509Certificate = keyEntry!!.certificate as X509Certificate
        val kInfo: KeyInfo = getKeyInfo(x509Cert, fac)
        val dsc = DOMSignContext(keyEntry!!.privateKey, xmlDoc.documentElement)
        val signature: XMLSignature = fac.newXMLSignature(sInfo, if (includeKeyInfo) kInfo else null)
        signature.sign(dsc)
        val node: Node = dsc.parent
        return node.ownerDocument
    }

    private fun getKeyInfo(cert: X509Certificate, fac: XMLSignatureFactory): KeyInfo {
        // Create the KeyInfo containing the X509Data.
        val kif: KeyInfoFactory = fac.keyInfoFactory

        val x509Content: MutableList<Any?> = ArrayList()
        x509Content.add(cert.subjectX500Principal.name)
        x509Content.add(cert)

        val xd: X509Data = kif.newX509Data(x509Content)
        return kif.newKeyInfo(listOf(xd))
    }

    private fun getKeyFromKeyStore(keyStoreFile: String, keyStorePassword: CharArray, alias: String): KeyStore.PrivateKeyEntry? {
        // Load the KeyStore and get the signing key and certificate.
        var keyFileStream: FileInputStream? = null

        return try {
            val ks = KeyStore.getInstance(KEY_STORE_TYPE)
            val filePath = "${System.getProperty("user.dir")}/${keyStoreFile}"
            keyFileStream = FileInputStream(filePath)
            ks.load(keyFileStream, keyStorePassword)
            ks.getEntry(alias, PasswordProtection(keyStorePassword)) as KeyStore.PrivateKeyEntry
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            if (keyFileStream != null) {
                try {
                    keyFileStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}