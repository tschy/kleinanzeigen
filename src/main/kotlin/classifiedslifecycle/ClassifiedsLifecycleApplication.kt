package classifiedslifecycle

import classifiedslifecycle.scraper.SearchConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ClassifiedsLifecycleApplication

fun main(args: Array<String>) {
    runApplication<ClassifiedsLifecycleApplication>(*args)
}

