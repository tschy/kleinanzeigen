package classifiedslifecycle

import classifiedslifecycle.model.SearchConfig
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class AppStartupRunner : ApplicationRunner {

    @Override
    override fun run(args: ApplicationArguments) {
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

}