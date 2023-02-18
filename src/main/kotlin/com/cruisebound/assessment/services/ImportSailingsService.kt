package com.cruisebound.assessment.services

import com.cruisebound.assessment.domains.Sailing

interface ImportSailingsService {
    fun addSailings(sailings: List<Sailing>)
}