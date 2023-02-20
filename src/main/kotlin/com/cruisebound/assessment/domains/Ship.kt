package com.cruisebound.assessment.domains

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "ships")
class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    var id: Long = 0
    var name: String? = null
    var rating: Double? = null
    var reviews: Int? = null
    var image: String? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    var line: Line? = Line()
}