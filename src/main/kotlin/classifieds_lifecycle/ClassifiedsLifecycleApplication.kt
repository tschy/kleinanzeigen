package classifieds_lifecycle

import classifieds_lifecycle.model.SearchConfig
import java.sql.*


fun main(args: Array<String>) {
    val fetcherService = FetcherService()
    val itemExtractor = ItemExtractor()
    val paginator = Paginator(fetcherService, itemExtractor)

    // set search parameters
    val config = SearchConfig(
        category = "fahrraeder",
        art = "herren",
        plz = "12309",
        searchTerm = "rennrad",
        radius = 10
    )
}

