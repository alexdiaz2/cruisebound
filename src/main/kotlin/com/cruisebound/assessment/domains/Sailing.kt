package com.cruisebound.assessment.domains

import com.cruisebound.assessment.utils.ItineraryConverter
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "sailings")
class Sailing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    var id: Long = 0
    var price: Double? = null
    var name: String? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    var ship: Ship = Ship()

    //  Because the Itinerary in the data file is an array of strings, we need to save it as varchar in the database
    @Convert(converter = ItineraryConverter::class, attributeName = "itinerary")
    var itinerary: List<String> = listOf()

    var region: String? = null
    @JsonFormat(pattern = "yyyy-MM-dd")
    var departureDate: LocalDate? = null
    @JsonFormat(pattern = "yyyy-MM-dd")
    var returnDate: LocalDate? = null
    var duration: Int? = null
}