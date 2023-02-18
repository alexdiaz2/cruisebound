package com.cruisebound.assessment.services

interface SailingService {
    fun searchSailings(
        page: Int,
        sortBy: String,
        sortOrder: String,
        price: Double?,
        departureDate: String?,
        duration: Int?,
        returnDate: String?
    ): HashMap<String, Any>
}