package com.cruisebound.assessment.bootstrap

import com.cruisebound.assessment.domains.Results
import com.cruisebound.assessment.repositories.SailingRepository
import com.cruisebound.assessment.services.SailingServiceImpl
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.anyList
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.springframework.core.io.ClassPathResource

class SailingsBootstrapTest {

    private lateinit var results: Results

    @InjectMocks
    lateinit var sailingService: SailingServiceImpl

    @Mock
    lateinit var sailingRepository: SailingRepository

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun run() {
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        val typeReference = object : TypeReference<Results>(){}
        val inputStream = ClassPathResource("data/sailingsData.json").inputStream
        results = mapper.readValue(inputStream, typeReference)

        assertThat(results.results).hasSize(100)

        `when`(sailingRepository.saveAll(anyList())).thenReturn(results.results)

        val sailings = sailingService.addSailings(results.results)

        assertNotNull(sailings)
        assertThat(sailings).hasSize(100)
    }
}