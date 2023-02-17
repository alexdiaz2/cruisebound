package com.cruisebound.assessment.domains

import jakarta.persistence.*

@Entity
@Table(name = "lines")
class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
    var logo: String? = null
    var name: String? = null
}