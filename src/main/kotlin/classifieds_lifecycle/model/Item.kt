package classifieds_lifecycle.model

import java.time.LocalDate

data class Item(
    val id: String,
    val title: String,
    val price: Double,
    val negotiable: Boolean,
    val created: LocalDate?,
)

