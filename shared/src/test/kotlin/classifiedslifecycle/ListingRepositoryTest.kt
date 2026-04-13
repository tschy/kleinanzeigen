package classifiedslifecycle

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.PostgreSQLContainer
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
        Assertions.assertThat(db.isRunning).isTrue
    }

    // db item exists
    // find by id
    // scrape count is bigger than it was before


    // save items from html file into db
    // assert that they exist, all non nullable fields are filled )

//    @Test
//    fun `id is generated when a genre is persisted`() {
//        val genre = Genre(name = "Poem")
//        assertThat(genre.id).isEqualTo(0L)
//        genreRepository.save(genre)
//        assertThat(genre.id).isNotEqualTo(0L)
//    }

}