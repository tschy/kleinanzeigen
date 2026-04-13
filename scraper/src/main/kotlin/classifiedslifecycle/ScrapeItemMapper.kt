package classifiedslifecycle

import java.time.Instant

fun toItem(scrapeItem: ScrapeItem): Item {
    val instant = Instant.now()
    return Item(
        ListingId(scrapeItem.id, instant),
        instant,
        1,
        scrapeItem.title,
        scrapeItem.price,
        scrapeItem.oldPrice,
        scrapeItem.negotiable,
        scrapeItem.created
    )
}
//
//fun Item.matches(scrapeItem: ScrapeItem): Boolean =
//    id.id == scrapeItem.id &&
//            title == scrapeItem.title &&
//            price == scrapeItem.price &&
//            negotiable == scrapeItem.negotiable &&
//            created == scrapeItem.created

fun Item.matches(scrapeItem: ScrapeItem): Boolean {
    val idMatch = id.id == scrapeItem.id
    val titleMatch = title == scrapeItem.title
    val priceMatch = price == scrapeItem.price
    val negotiableMatch = negotiable == scrapeItem.negotiable
    val createdMatch = created == scrapeItem.created
    if (!idMatch || !titleMatch || !priceMatch || !negotiableMatch || !createdMatch) {
        println("matches failed: id=$idMatch title=$titleMatch price=$priceMatch negotiable=$negotiableMatch created=$createdMatch")
        println("  db:      $this")
        println("  scraped: $scrapeItem")
    }
    return idMatch && titleMatch && priceMatch && negotiableMatch && createdMatch
}