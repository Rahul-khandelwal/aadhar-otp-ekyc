package com.rahul.aadhar.config

import org.springframework.context.annotation.Configuration
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.rahul.aadhar.utils.DigitalSigner
import com.rahul.aadhar.utils.Encryptor
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

@Configuration
class ApplicationConfig {

    @Bean
    fun restTemplate(): RestTemplate {
        val restTemplate = RestTemplate()

        // Replace the ObjectMapper in MappingJackson2HttpMessageConverter
        for (i in 0..restTemplate.messageConverters.size) {
            val converter = restTemplate.messageConverters[i]

            if (converter is MappingJackson2HttpMessageConverter) {
                converter.objectMapper = ObjectMapper().registerKotlinModule()
                break
            }
        }

        return restTemplate
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun applicationProperties() : ApplicationProperties = ApplicationProperties()

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun digitalSigner() : DigitalSigner = DigitalSigner()

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun encryptor() : Encryptor = Encryptor()
}