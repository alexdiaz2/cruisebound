package com.cruisebound.assessment.services

import com.cruisebound.assessment.domains.Sailing
import org.springframework.data.domain.Page

interface SailingService {
    fun addSailings(sailings: List<Sailing>): MutableList<Sailing>

    fun searchSailings(
        page: Int,
        sortBy: String,
        sortOrder: String,
        price: Double?,
        departureDate: String?,
        duration: Int?,
        returnDate: String?
    ): Page<Sailing>
}