package com.cruisebound.assessment.domains

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "lines")
class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    var id: Long = 0
    var logo: String? = null
    var name: String? = null
}