package classifiedslifecycle.scraper

data class SearchConfig(
    val category: String,
    val art: String,
    val plz: String,
    val searchTerm: String,
    val radius: Int
)