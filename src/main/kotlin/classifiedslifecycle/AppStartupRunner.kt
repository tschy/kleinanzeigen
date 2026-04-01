package classifiedslifecycle

import classifiedslifecycle.model.SearchConfig
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!test")
class AppStartupRunner(
    private val itemService: ItemService
) : ApplicationRunner {

    @Override
    override fun run(args: ApplicationArguments) {

        // set search parameters
        val config = SearchConfig(
            category = "fahrraeder",
            art = "herren",
            plz = "12309",
            searchTerm = "rennrad",
            radius = 10
        )

        val scrapeItems = Paginator(FetcherService(), ItemExtractor()).run(config)
        itemService.process(scrapeItems)

    }

}