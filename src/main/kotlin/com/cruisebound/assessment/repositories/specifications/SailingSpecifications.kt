package com.cruisebound.assessment.repositories.specifications

import com.cruisebound.assessment.domains.Sailing
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDate

class SailingSpecifications {
    fun toPredicate(price: Double?, departureDate: String?, duration: Int?, returnDate: String?): Specification<Sailing> {
        return Specification { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()

            if (price != null) {
                predicates.add(criteriaBuilder.equal(root.get<Sailing>("price"), price))
            }
            if (departureDate != null) {
                predicates.add(criteriaBuilder.equal(root.get<Sailing>("departureDate"), LocalDate.parse(departureDate)))
            }
            if (duration != null) {
                predicates.add(criteriaBuilder.equal(root.get<Sailing>("duration"), duration))
            }
            if (returnDate != null) {
                predicates.add(criteriaBuilder.equal(root.get<Sailing>("returnDate"), LocalDate.parse(returnDate)))
            }

            criteriaBuilder.and(*predicates.toTypedArray())
        }
    }
}