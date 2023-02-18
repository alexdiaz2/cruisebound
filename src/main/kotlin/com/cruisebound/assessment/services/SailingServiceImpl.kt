package com.cruisebound.assessment.services

import com.cruisebound.assessment.repositories.SailingRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class SailingServiceImpl(private val sailingRepository: SailingRepository) : SailingService {
    override fun searchSailings(
        page: Int,
        sortBy: String,
        sortOrder: String,
        price: Double?,
        departureDate: String?,
        duration: Int?,
        returnDate: String?
    ): HashMap<String, Any> {
        val pageNumber = if (page > 0)
            page - 1
        else
            page

        val sort = if (sortOrder != "desc")
            Sort.by(sortBy)
        else
            Sort.by(Sort.Order.desc(sortBy))

        val paging: Pageable = PageRequest.of(pageNumber, 10, sort)

        val result = sailingRepository.findAll(paging)

        val response = HashMap<String, Any>()
        response["results"] = result.content
        response["currentPage"] = result.number + 1
        response["totalResults"] = result.totalElements
        response["totalPages"] = result.totalPages

        return response
    }
}