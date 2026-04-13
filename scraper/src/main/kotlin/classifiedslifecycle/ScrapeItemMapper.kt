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

fun Item.matches(scrapeItem: ScrapeItem): Boolean =
    id.id == scrapeItem.id &&
            title == scrapeItem.title &&
            price == scrapeItem.price &&
            negotiable == scrapeItem.negotiable &&
            created == scrapeItem.created