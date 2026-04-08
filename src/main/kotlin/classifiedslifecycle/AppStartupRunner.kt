package classifiedslifecycle

import classifiedslifecycle.model.SearchConfig
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
//@Profile("!test") // anschalten um nur Testklassen laufen zu lassen
class AppStartupRunner(
    private val itemService: ItemService,
    private val paginator: Paginator
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

        val scrapeItems = paginator.run(config)
        println("found ${scrapeItems.size} items")
        itemService.process(scrapeItems)

    }

}