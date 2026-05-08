package classifiedslifecycle

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface SearchConfigRepository : JpaRepository<SearchConfig, Int> {

    fun findByCategoryAndArtAndPlzAndSearchTermAndRadius(
        category: String,
        art: String,
        plz: String,
        searchTerm: String,
        radius: Int
    ): SearchConfig?

}