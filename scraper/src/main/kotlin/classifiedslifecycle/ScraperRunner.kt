package classifiedslifecycle

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.annotation.Profile

//@Profile("!test & !test-m") //prevents scraping into the prod db when tests are running
@Component
class ScraperRunner(
    private val itemService: ItemService,
    private val paginator: Paginator
) : ApplicationRunner {
    private val logger = KotlinLogging.logger {}

    override fun run(args: ApplicationArguments) {
        logger.info { "---running the scraper" }
        val config = SearchConfig(
            category = "fahrraeder",
            art = "herren",
            plz = "12309",
            searchTerm = "rennrad",
            radius = 10
        )
        val scrapeItems = paginator.run(config)
        logger.info { "found ${scrapeItems.size} items" }
        itemService.process(scrapeItems)
    }
}