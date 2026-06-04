package classifiedslifecycle

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

val ourObjectMapper = JsonMapper.builder()
    .configure(MapperFeature.ALLOW_COERCION_OF_SCALARS, false)
    .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
    .build()

@Service
interface SearchConfigLoader {
    val searchConfigRepository: SearchConfigRepository

    fun getJsonStrings(): List<String>

    fun getActiveConfigsString(): String

    fun getConfigs(): List<SearchConfig> {
        val configs = mutableListOf<SearchConfig>()

        val activeConfigs = ourObjectMapper
            .readValue<List<String>>(getActiveConfigsString())

        getJsonStrings().forEach() { jsonString ->
            val configRead = ourObjectMapper.readValue<SearchConfig>(
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

