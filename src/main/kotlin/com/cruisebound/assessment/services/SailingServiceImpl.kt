package com.cruisebound.assessment.services

import com.cruisebound.assessment.domains.Sailing
import com.cruisebound.assessment.repositories.SailingRepository
import com.cruisebound.assessment.repositories.specifications.SailingSpecifications
import org.springframework.data.domain.Page
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

        val result : Page<Sailing> = if (price == null && departureDate == null && duration == null && returnDate == null) {
            sailingRepository.findAll(paging)
        } else {
            sailingRepository.findAll(SailingSpecifications().toPredicate(price, departureDate, duration, returnDate), paging)
        }

        val response = HashMap<String, Any>()
        response["results"] = result.content
        response["currentPage"] = result.number + 1
        response["totalResults"] = result.totalElements
        response["totalPages"] = result.totalPages

        return response
    }
}