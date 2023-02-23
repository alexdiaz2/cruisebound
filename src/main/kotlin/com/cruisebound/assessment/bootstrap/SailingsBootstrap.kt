package com.cruisebound.assessment.bootstrap

import com.cruisebound.assessment.domains.Results
import com.cruisebound.assessment.services.SailingService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.boot.CommandLineRunner
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class SailingsBootstrap(private val sailingService: SailingService) : CommandLineRunner {
    override fun run(vararg args: String?) {
        importSailingsData()
    }

    private fun importSailingsData() {
        val mapper = ObjectMapper()

        //  Added for LocalDate mapping compatibility
        mapper.registerModule(JavaTimeModule())

        val typeReference = object : TypeReference<Results>(){}
        val inputStream = ClassPathResource("data/sailingsData.json").inputStream

        val results = mapper.readValue(inputStream, typeReference)
        sailingService.addSailings(results.results)
    }
}