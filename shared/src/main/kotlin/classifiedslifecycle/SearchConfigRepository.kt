package classifiedslifecycle

import jdk.jfr.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SearchConfigRepository : JpaRepository<SearchConfig, Int> {
    fun findByNameAndCategoryAndArtAndPlzAndSearchTermAndRadius(
        name: String,
        category: String,
        art: String,
        plz: String,
        searchTerm: String,
        radius: Int?
    ): SearchConfig?
}