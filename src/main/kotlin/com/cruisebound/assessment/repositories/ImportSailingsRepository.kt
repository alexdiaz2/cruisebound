package com.cruisebound.assessment.repositories

import com.cruisebound.assessment.domains.Sailing
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ImportSailingsRepository : CrudRepository<Sailing, Long> {
}