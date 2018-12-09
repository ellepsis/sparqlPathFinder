package com.ellepsis.comunicaPathFinder.comunicaPathFinder.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter


/**
 * @author Ilya Mikhailov (Ilya.Mikhailov@lanit-tercom.com) created on 04-Dec-18.
 */
@Configuration
class Config {

    @Bean
    fun mappingJackson2HttpMessageConverter(): MappingJackson2HttpMessageConverter {
        val mapper = ObjectMapper()
        mapper.registerModule(KotlinModule())
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        return MappingJackson2HttpMessageConverter(mapper)
    }
}