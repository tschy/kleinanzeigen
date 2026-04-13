package classifiedslifecycle

import classifiedslifecycle.analysis.Analyser
import classifiedslifecycle.scraper.ItemService
import classifiedslifecycle.scraper.SearchConfig
import classifiedslifecycle.scraper.Paginator
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
//@Profile("!test") // anschalten, um nur Testklassen laufen zu lassen
//@Profile("!test-m") // anschalten, um nur Testklassen laufen zu lassen
class AppStartupRunner(
    private val itemService: ItemService,
    private val paginator: Paginator,
    private val analyser: Analyser
) : ApplicationRunner {

    private val logger = KotlinLogging.logger {}

    @Override
    override fun run(args: ApplicationArguments) {

        logger.info {"---running the scraper"}
        // set search parameters
        val config = SearchConfig(
            category = "fahrraeder",
            art = "herren",
            plz = "12309",
            searchTerm = "rennrad",
            radius = 10
        )


        val scrapeItems = paginator.run(config)
        println("found ${scrapeItems.size} items")
        itemService.process(scrapeItems)

        analyser.analyse()

    }

}