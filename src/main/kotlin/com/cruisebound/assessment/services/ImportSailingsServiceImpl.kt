package com.cruisebound.assessment.services

import com.cruisebound.assessment.domains.Sailing
import com.cruisebound.assessment.repositories.ImportSailingsRepository
import org.springframework.stereotype.Service

@Service
class ImportSailingsServiceImpl(private val importSailingsRepository: ImportSailingsRepository) : ImportSailingsService {
    override fun addSailings(sailings: List<Sailing>) {
        importSailingsRepository.saveAll(sailings)
    }
}