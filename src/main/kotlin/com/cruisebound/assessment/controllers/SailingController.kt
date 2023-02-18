package com.cruisebound.assessment.controllers

import com.cruisebound.assessment.services.SailingService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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


}