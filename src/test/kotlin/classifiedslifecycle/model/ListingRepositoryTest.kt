package classifiedslifecycle.model

import classifiedslifecycle.ListingRepository
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


// uses Testcontainers, container is started once per test class and stopped afterwards
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class ListingRepositoryTest {

    @Autowired
    lateinit var listingRepository: ListingRepository

    companion object {
        @Container
        @JvmStatic
        @ServiceConnection
        val db = PostgreSQLContainer("postgres:18-alpine")
    }

    @Test
    fun dbRunning() {
        Assertions.assertThat(db.isRunning)
    }

//    @Test
//    fun `id is generated when a genre is persisted`() {
//        val genre = Genre(name = "Poem")
//        assertThat(genre.id).isEqualTo(0L)
//        genreRepository.save(genre)
//        assertThat(genre.id).isNotEqualTo(0L)
//    }

}