package classifiedslifecycle

import classifiedslifecycle.model.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ListingRepository : JpaRepository<Item, ListingId>
