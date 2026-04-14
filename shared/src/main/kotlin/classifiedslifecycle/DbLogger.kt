package classifiedslifecycle

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class DbLogger(val dataSource: DataSource) : ApplicationRunner {
    private val logger = KotlinLogging.logger {}
    override fun run(args: ApplicationArguments) {
        logger.info { "Connected to DB: ${dataSource.connection.metaData.url}" }
    }
}