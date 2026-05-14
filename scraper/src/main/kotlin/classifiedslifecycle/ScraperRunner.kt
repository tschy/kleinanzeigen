package classifiedslifecycle

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.io.ClassPathResource
import com.fasterxml.jackson.module.kotlin.readValue

//@Profile("!test & !test-m") //prevents scraping into the db when tests are running
@Component
class ScraperRunner(
    private val itemService: ItemService,
    private val paginator: Paginator,
    private val scrapeRepository: ScrapeRepository,
    private val searchConfigService: SearchConfigService
) : ApplicationRunner {
    private val logger = KotlinLogging.logger {}

    override fun run(args: ApplicationArguments) {
        logger.info { "---running the scraper" }

        val configs = searchConfigService.readConfigs()

        configs.forEach { config ->

            val scrapeItems = paginator.run(config)
            logger.info { "Found ${scrapeItems.size} items in total (top ads are disregarded)" }
            itemService.process(scrapeItems, config)

            if (!scrapeItems.isEmpty()) {
                scrapeRepository
                    .save(Scrape(
                        0,
                        scrapeItems.first().scrapeTime,
                        scrapeItems.size,
                        config)
                    )
            }
        }
    }
}