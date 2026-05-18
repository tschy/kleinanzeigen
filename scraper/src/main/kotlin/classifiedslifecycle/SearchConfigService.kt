package classifiedslifecycle

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
interface SearchConfigService {

    val searchConfigRepository: SearchConfigRepository

    fun getConfigsAsJsonStrings(): List<String>

    fun getConfigs(): List<SearchConfig>  {
        val configs = mutableListOf<SearchConfig>()

        getConfigsAsJsonStrings().forEach { string ->

            val mapper = jacksonObjectMapper()
            val configRead = mapper.readValue<SearchConfig>(string)

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
                " using config ${existing?.toDebugString()}" }

            val config = existing ?: searchConfigRepository
                .save(configRead)

            configs.add(config)
        }
        return configs
    }
}

