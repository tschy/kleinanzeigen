package classifiedslifecycle

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
interface SearchConfigLoader {

    val searchConfigRepository: SearchConfigRepository

    fun getJsonStrings(): List<String>

    fun getActiveConfigsString(): String

    fun getConfigs(): List<SearchConfig> {
        val configs = mutableListOf<SearchConfig>()

        val activeConfigs = jacksonObjectMapper()
            .readValue<List<String>>(getActiveConfigsString())

        getJsonStrings().forEach() { jsonString ->
            val mapper = jacksonObjectMapper()
            val configRead = mapper.readValue<SearchConfig>(
                jsonString
            )

            if (configRead.name in activeConfigs) {
                val existing = searchConfigRepository
                    .findByNameAndCategoryAndArtAndPlzAndSearchTermAndRadius(
                        configRead.name,
                        configRead.category,
                        configRead.art,
                        configRead.plz,
                        configRead.searchTerm,
                        configRead.radius
                    )

                KotlinLogging.logger {}.info {
                    " using config ${existing?.toDebugString()}"
                }

                val config = existing ?: searchConfigRepository
                    .save(configRead)

                configs.add(config)
            }
        }
        return configs
    }
}

