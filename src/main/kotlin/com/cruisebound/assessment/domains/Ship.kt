package com.cruisebound.assessment.domains

import jakarta.persistence.*

@Entity
@Table(name = "ships")
class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
    var name: String? = null
    var rating: Double? = null
    var reviews: Int? = null
    var image: String? = null

    @ManyToOne
    var line: Line? = Line()
}