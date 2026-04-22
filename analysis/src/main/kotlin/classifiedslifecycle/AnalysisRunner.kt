package classifiedslifecycle

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import io.github.oshai.kotlinlogging.KotlinLogging

@Component
class AnalysisRunner(
    private val analyser: Analyser,
    private val dbTest: DbConnectionTest

) : ApplicationRunner {
    private val logger = KotlinLogging.logger {}

    override fun run(args: ApplicationArguments) {
        logger.info { "---running the analyser" }

        analyser.debugPrintAggregatedItems()
        analyser.printTableDiscountsOnlineAndOffline()

    }
}