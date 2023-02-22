package com.cruisebound.assessment.controllers

import com.cruisebound.assessment.domains.Results
import com.cruisebound.assessment.domains.Sailing
import com.cruisebound.assessment.services.SailingService
import org.springframework.data.mapping.PropertyReferenceException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.format.DateTimeParseException

@RestController
@RequestMapping("/api/v1/sailings")
class SailingController(private val sailingService: SailingService) {

    @GetMapping("/search")
    fun searchSailings(@RequestParam(defaultValue = "0") page: Int,
                       @RequestParam(defaultValue = "id") sortBy: String,
                       @RequestParam(defaultValue = "asc") sortOrder: String,
                       @RequestParam(required = false) price: Double?,
                       @RequestParam(required = false) departureDate: String?,
                       @RequestParam(required = false) duration: Int?,
                       @RequestParam(required = false) returnDate: String?,
                       @RequestParam(required = false) allParams: Map<String, String>) : ResponseEntity<HashMap<String, Any>> {
        if (allParams.size > 8) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "To many parameters"
            )
        }
        if (sortOrder.isNotEmpty() && sortOrder.lowercase() != "asc" && sortOrder.lowercase() != "desc") {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Unsupported order"
            )
        }
        if (allParams.isNotEmpty()) {
            for (parameter in allParams.keys) {
                if (parameter != "page" && parameter != "sortBy" && parameter != "sortOrder" && parameter != "price"
                    && parameter != "departureDate" && parameter != "duration" && parameter != "returnDate") {
                    throw ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "The parameter $parameter is not allowed"
                    )
                }
            }
        }

        val result = sailingService.searchSailings(page, sortBy.lowercase(), sortOrder.lowercase(), price, departureDate, duration, returnDate)
        val response = HashMap<String, Any>()
        response["results"] = result.content
        response["currentPage"] = result.number + 1
        response["totalResults"] = result.totalElements
        response["totalPages"] = result.totalPages

        return ResponseEntity<HashMap<String, Any>>(response, HttpStatus.OK)
    }

    //  API created in case you want to import more sailings
    @PostMapping("/import")
    fun importSailings(@RequestBody resultList: Results): ResponseEntity<MutableList<Sailing>> {
        val sailingList = sailingService.addSailings(resultList.results)
        return ResponseEntity<MutableList<Sailing>>(sailingList, HttpStatus.CREATED)
    }

    //  Sometimes is better to avoid returning too much information about the error (like queries, models, id's...)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PropertyReferenceException::class)
    fun handlePropertyReferenceException(exception: PropertyReferenceException?): HashMap<String, String> {
        val error = HashMap<String, String>()
        error["error"] = "Can't complete the search with the provided parameters, please try with different value"
        return error
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DateTimeParseException::class)
    fun handleDateTimeParseException(exception: DateTimeParseException?): HashMap<String, String> {
        val error = HashMap<String, String>()
        error["error"] = "The date you provided is not in the correct format, please try again with yyyy-MM-dd"
        return error
    }
}