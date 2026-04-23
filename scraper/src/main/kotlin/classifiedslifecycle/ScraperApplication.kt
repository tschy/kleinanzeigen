package classifiedslifecycle

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@SpringBootApplication
class ScraperApplication

fun main(args: Array<String>) {

    val context = runApplication<ScraperApplication>(*args)
}