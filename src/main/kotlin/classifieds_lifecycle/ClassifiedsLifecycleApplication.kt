package classifieds_lifecycle

import classifieds_lifecycle.model.SearchConfig


fun main(args: Array<String>) {
    val fetcherService = FetcherService()
    val itemExtractor = ItemExtractor()
    val paginator = Paginator(fetcherService, itemExtractor)

    val config = SearchConfig(
        category = "fahrraeder",
        art = "herren",
        plz = "12309",
        searchTerm = "rennrad",
        radius = 10
    )

    paginator.run(config)
}