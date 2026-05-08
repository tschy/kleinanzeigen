package classifiedslifecycle

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SearchConfigRepository : JpaRepository<SearchConfig, Int> {
    fun findByName(name: String): SearchConfig?
}