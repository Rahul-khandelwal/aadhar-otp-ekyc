package com.rahul.aadhar.auth._2_5.request

import java.io.StringWriter
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlValue


/**
 * Format -
 * <Skey ci="">encrypted and encoded session key</Skey>
 *
 * We are using JAXB here to get proper value of session key in XML
 */
@XmlRootElement
class Skey(@XmlValue private val value: ByteArray, @XmlAttribute(name = "ci") private val ci: String) {

    constructor() : this("".toByteArray(), "")

    fun toXmlRequest() : String {
        val jaxbContext = JAXBContext.newInstance(Skey::class.java)
        val jaxbMarshaller = jaxbContext.createMarshaller()
        jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true)
        val sw = StringWriter()
        jaxbMarshaller.marshal(this, sw)
        return sw.toString()
    }
}
