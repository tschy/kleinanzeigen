package classifiedslifecycle

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "search_config")
open class SearchConfig(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val category: String,
    val art: String,
    val plz: String,
    val searchTerm: String,
    val radius: Int

) {
    fun toDebugString() =
    "'${id}' " +
            ": '${category}' " +
            ": '${art}' " +
            ": '${plz}' " +
            ": '${searchTerm}' " +
            ": '${radius}' "
}