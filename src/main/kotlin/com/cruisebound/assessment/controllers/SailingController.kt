package com.cruisebound.assessment.controllers

import com.cruisebound.assessment.services.SailingService
import org.springframework.data.mapping.PropertyReferenceException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
                       @RequestParam(required = false) returnDate: String?) : ResponseEntity<Map<String, Any>> {
        val response = sailingService.searchSailings(page, sortBy,sortOrder, price, departureDate, duration, returnDate)
        return ResponseEntity<Map<String, Any>>(response, HttpStatus.OK)
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
        error["error"] = "The date yoi provided is not in the correct format, please try again with yyyy-MM-dd"
        return error
    }
}