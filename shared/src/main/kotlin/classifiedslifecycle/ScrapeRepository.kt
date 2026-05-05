package classifiedslifecycle

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface ScrapeRepository : JpaRepository<Scrape, Int> {


}