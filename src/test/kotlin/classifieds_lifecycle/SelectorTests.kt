package classifieds_lifecycle

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.ssl.SslProperties
import java.io.File


class SelectorTests {

    // print sto
    val fetcherTestObject = FetcherService()

    @Test
    fun testSelector() {

        val text = File("src/test/resources/rennraeder20260319.htm")
             .readText()
        //   ?: error("File not found!")
        // println(text)



        val results = fetcherTestObject.extract(text)
        assertThat(results).hasSize(27)
       //assertEquals("", "/s-anzeige/giant-escape-1-trekkingrad-cityrad/3190643254-217-3354")

    }
}