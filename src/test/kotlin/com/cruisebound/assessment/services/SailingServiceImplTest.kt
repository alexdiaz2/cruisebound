package com.cruisebound.assessment.services

import com.cruisebound.assessment.domains.Results
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SpringBootTest
class SailingServiceImplTest {

    @Autowired
    lateinit var sailingServiceImpl: SailingServiceImpl

    @Test
    @Order(1)
    fun addSailings() {
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        val typeReference = object : TypeReference<Results>(){}
        val inputStream = ClassPathResource("data/sailingsData.json").inputStream
        val results = mapper.readValue(inputStream, typeReference)

        val savedSailings = sailingServiceImpl.addSailings(results.results)
        assertThat(savedSailings).isNotNull
        assertThat(savedSailings).isNotEmpty
        assertThat(savedSailings).hasSize(100)
    }

    @Test
    fun searchSailingsExample1() {
        assertThat(sailingServiceImpl.searchSailings(1, "id", "asc", null, null, null, null))
            .isNotNull
            .isNotEmpty
            .hasSize(10)
    }

    @Test
    fun searchSailingsExample2() {
        val sailingsPage = sailingServiceImpl.searchSailings(5, "id", "asc", null, null, null, null)
        assertEquals(sailingsPage.number + 1, 5)
    }

    @Test
    fun searchSailingsExample3() {
        val sailingsPage = sailingServiceImpl.searchSailings(1, "price", "asc", null, null, null, null)
        assertThat(sailingsPage.content[0].price!! > sailingsPage.content[4].price!!)
    }

    @Test
    fun searchSailingsExample4() {
        val sailingsPage = sailingServiceImpl.searchSailings(1, "price", "desc", null, null, null, null)
        assertThat(sailingsPage.content[0].price!! < sailingsPage.content[4].price!!)
    }

    @Test
    fun searchSailingsExample5() {
        val sailingsPage = sailingServiceImpl.searchSailings(1, "price", "desc", null, "2022-11-26", null, null)
        val localDate = LocalDate.parse("2022-11-26", DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        assertThat(sailingsPage.totalElements < 50)
        assertThat(sailingsPage.content[0].price!! < sailingsPage.content[4].price!!)
        for (sailing in sailingsPage.content) {
            assertEquals(sailing.departureDate, localDate)
        }
    }

    @Test
    fun searchSailingsExample6() {
        val sailingsPage = sailingServiceImpl.searchSailings(1, "id", "asc", 999.0, "2022-11-26", null, null)
        val localDate = LocalDate.parse("2022-11-26", DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        for (sailing in sailingsPage.content) {
            assertEquals(sailing.price, 999.0)
            assertEquals(sailing.departureDate, localDate)
        }
    }

    @Test
    fun searchSailingsExample7() {
        val sailingsPage = sailingServiceImpl.searchSailings(1, "price", "desc", 429.0, "2022-11-26", 5, "2022-12-01")
        val localDateDeparture = LocalDate.parse("2022-11-26", DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val localDateReturn = LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        for (sailing in sailingsPage.content) {
            assertEquals(sailing.price, 429.0)
            assertEquals(sailing.departureDate, localDateDeparture)
            assertEquals(sailing.duration, 5)
            assertEquals(sailing.returnDate, localDateReturn)
        }
    }
}