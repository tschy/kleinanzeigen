package classifiedslifecycle

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class DbConnectionTest {

    @Bean
    fun testDbConnection(dataSource: DataSource) = ApplicationRunner {
        val connection = dataSource.connection
        val result = connection.createStatement().executeQuery("SELECT 1")
        result.next()
        println("✅ DB connected! Result: ${result.getInt(1)}")
        connection.close()
    }
}