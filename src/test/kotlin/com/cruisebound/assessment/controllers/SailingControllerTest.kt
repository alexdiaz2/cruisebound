package com.cruisebound.assessment.controllers

import com.cruisebound.assessment.domains.Results
import com.cruisebound.assessment.domains.Sailing
import com.cruisebound.assessment.services.SailingServiceImpl
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.jayway.jsonpath.JsonPath
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.data.domain.*
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors


@SpringBootTest
@AutoConfigureMockMvc
class SailingControllerTest {

    lateinit var mockMvc: MockMvc

    lateinit var controller: SailingController

    lateinit var results: Results

    @Mock
    lateinit var sailingService: SailingServiceImpl

    @BeforeEach
    fun setUp() {
        //  Loading data from file
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        val typeReference = object : TypeReference<Results>(){}
        val inputStream = ClassPathResource("data/sailingsData.json").inputStream
        results = mapper.readValue(inputStream, typeReference)

        MockitoAnnotations.openMocks(this)
        controller = SailingController(sailingService)
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }

    @Test
    @Order(1)
    fun importSailings() {
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        val request = Results()
        request.results.addAll(results.results)

        `when`(sailingService.addSailings(anyList())).thenReturn(request.results)

        mockMvc.post("/api/v1/sailings/import") {
            content = mapper.writeValueAsString(request)
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.ALL
        }
            .andExpect {
                status { isCreated() }
                jsonPath("$.size()") { value(100) }
            }
    }

    @Test
    @Order(2)
    fun searchSailingsExample1() {
        `when`(sailingService.searchSailings(1, "id", "asc", null,
            null, null, null))
            .thenReturn(resultsToPage(1, "id", "asc", results.results))

        mockMvc.get("/api/v1/sailings/search") {
            param("page", "1")
            param("sortBy", "id")
            param("sortOrder", "asc")
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.totalPages") { value(10) }
            }
    }


    @Test
    @Order(3)
    fun searchSailingsExample2() {
        val page = 5

        `when`(sailingService.searchSailings(page, "id", "asc", null,
            null, null, null))
            .thenReturn(resultsToPage(page - 1, "id", "asc", results.results))

        mockMvc.get("/api/v1/sailings/search") {
            param("page", "$page")
            param("sortBy", "id")
            param("sortOrder", "asc")
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.currentPage") { value(5 - 1) }
            }
    }

    @Test
    @Order(4)
    fun searchSailingsExample3() {
        `when`(sailingService.searchSailings(1, "price", "asc", null,
            null, null, null))
            .thenReturn(resultsToPage(1, "price", "asc", results.results))

        val mvcResult = mockMvc.get("/api/v1/sailings/search") {
            param("page", "1")
            param("sortBy", "price")
            param("sortOrder", "asc")
        }
            .andExpect {
                status { isOk() }
            }
            .andReturn()

        val firstPrice = JsonPath.read<Double>(mvcResult.response.contentAsString, "$.results[0].price")
        val secondPrice = JsonPath.read<Double>(mvcResult.response.contentAsString, "$.results[4].price")
        assertThat(firstPrice > secondPrice)
    }

    @Test
    @Order(5)
    fun searchSailingsExample4() {
        `when`(sailingService.searchSailings(1, "price", "desc", null,
            null, null, null))
            .thenReturn(resultsToPage(1, "price", "desc", results.results))

        val mvcResult = mockMvc.get("/api/v1/sailings/search") {
            param("page", "1")
            param("sortBy", "price")
            param("sortOrder", "desc")
        }
            .andExpect {
                status { isOk() }
            }
            .andReturn()

        val firstPrice = JsonPath.read<Double>(mvcResult.response.contentAsString, "$.results[0].price")
        val secondPrice = JsonPath.read<Double>(mvcResult.response.contentAsString, "$.results[4].price")
        assertThat(firstPrice < secondPrice)
    }

    @Test
    fun searchSailingsExample5() {
        val localDepartureDate = LocalDate.parse("2022-11-26", DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val filteredResults = results.results.stream().filter { x -> x.departureDate!! == localDepartureDate }.collect(Collectors.toList())

        `when`(sailingService.searchSailings(1, "price", "desc", null,
            "2022-11-26", null, null))
            .thenReturn(resultsToPage(1, "price", "desc", filteredResults))

        val mvcResult = mockMvc.get("/api/v1/sailings/search") {
            param("page", "1")
            param("sortBy", "price")
            param("sortOrder", "desc")
            param("departureDate", "2022-11-26")
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.totalResults") { value(filteredResults.size) }
            }
            .andReturn()

        val firstPrice = JsonPath.read<Double>(mvcResult.response.contentAsString, "$.results[0].price")
        val secondPrice = JsonPath.read<Double>(mvcResult.response.contentAsString, "$.results[4].price")
        assertThat(firstPrice < secondPrice)

        for (i in 0 until filteredResults.size) {
            assertEquals(
                LocalDate.parse(
                    JsonPath.read<String>(mvcResult.response.contentAsString, "$.results[$i].departureDate"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                ),
                localDepartureDate
            )
        }
    }

    @Test
    fun searchSailingsExample6() {
        val price = 999.9
        val localDepartureDate = LocalDate.parse("2022-11-26", DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val filteredResults = results.results.stream().filter { sailing ->
            sailing.departureDate!! == localDepartureDate && sailing.price!!.equals(price)
        }.collect(Collectors.toList())

        `when`(sailingService.searchSailings(1, "id", "asc", price,
            "2022-11-26", null, null))
            .thenReturn(resultsToPage(1, "price", "desc", filteredResults))

        val mvcResult = mockMvc.get("/api/v1/sailings/search") {
            param("page", "1")
            param("sortBy", "id")
            param("sortOrder", "asc")
            param("price", "$price")
            param("departureDate", "2022-11-26")
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.totalResults") { value(filteredResults.size) }
            }
            .andReturn()

        for (i in 0 until filteredResults.size) {
            assertEquals(
                LocalDate.parse(
                    JsonPath.read<String>(mvcResult.response.contentAsString, "$.results[$i].departureDate"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                ),
                localDepartureDate
            )
            assertEquals(JsonPath.read(mvcResult.response.contentAsString, "$.results[$i].price"),
                price
            )
        }
    }

    @Test
    fun searchSailingsExample7() {
        val price = 429.0
        val localDepartureDate = LocalDate.parse("2022-11-26", DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val localReturnDate = LocalDate.parse("2022-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val duration = 5

        val filteredResults = results.results.stream().filter { sailing ->
            sailing.departureDate!! == localDepartureDate &&
            sailing.price!!.equals(price) &&
            sailing.duration!! == duration &&
            sailing.returnDate!! == localReturnDate
        }.collect(Collectors.toList())

        `when`(sailingService.searchSailings(1, "price", "desc", price,
            "2022-11-26", duration, "2022-12-01"))
            .thenReturn(resultsToPage(1, "price", "desc", filteredResults))

        val mvcResult = mockMvc.get("/api/v1/sailings/search") {
            param("page", "1")
            param("sortBy", "price")
            param("sortOrder", "desc")
            param("price", "$price")
            param("departureDate", "2022-11-26")
            param("duration", "$duration")
            param("returnDate", "2022-12-01")
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.totalResults") { value(filteredResults.size) }
            }
            .andReturn()

        for (i in 0 until filteredResults.size) {
            assertEquals(
                LocalDate.parse(
                    JsonPath.read<String>(mvcResult.response.contentAsString, "$.results[$i].departureDate"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                ),
                localDepartureDate
            )
            assertEquals(
                LocalDate.parse(
                    JsonPath.read<String>(mvcResult.response.contentAsString, "$.results[$i].returnDate"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                ),
                localReturnDate
            )
            assertEquals(JsonPath.read(mvcResult.response.contentAsString, "$.results[$i].price"), price)
            assertEquals(JsonPath.read(mvcResult.response.contentAsString, "$.results[$i].duration"), duration)
        }
    }

    fun resultsToPage(page: Int, sortBy: String, sortOrder: String, resultList: MutableList<Sailing>): Page<Sailing> {

        val pageNumber = if (page > 0)
            page - 1
        else
            page

        val sort = if (sortOrder != "desc")
            Sort.by(sortBy)
        else
            Sort.by(Sort.Order.desc(sortBy))

        val paging: Pageable = PageRequest.of(pageNumber, 10, sort)

        return PageImpl(resultList, paging, resultList.size.toLong())
    }
}