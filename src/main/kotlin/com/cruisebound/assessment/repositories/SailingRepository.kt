package com.cruisebound.assessment.repositories

import com.cruisebound.assessment.domains.Sailing
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface SailingRepository : JpaRepository<Sailing, Long>, JpaSpecificationExecutor<Sailing>