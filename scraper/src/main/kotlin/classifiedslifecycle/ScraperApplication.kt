package classifiedslifecycle

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@SpringBootApplication
class ScraperApplication

fun main(args: Array<String>) {
    // 1. Start the context
    val context = runApplication<ScraperApplication>(*args)

    try {
        // 2. Your scraping logic runs here automatically
        // (assuming it's in a @Scheduled task or a CommandLineRunner)
    } finally {
        // 3. Close the context to release DB connections
        val exitCode = SpringApplication.exit(context)
        // 4. Kill the JVM so the container stops immediately
        exitProcess(exitCode)
    }
}