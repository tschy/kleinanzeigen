package classifiedslifecycle

import classifiedslifecycle.analysis.Analyser
import classifiedslifecycle.shared.ListingRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

// Integration test (@SpringBootTest) using a separate test database specified in application-test.properties.

@SpringBootTest
@ActiveProfiles("test")
//@Transactional  // rollback changes
class AnalyserTest {

    @Autowired
    lateinit var listingRepository: ListingRepository


    @Test
    fun analyseTest() {
        val analyser = Analyser(listingRepository)
        analyser.analyse()
    }
}