package classifieds_lifecycle


fun main(args: Array<String>) {
	val itemExtractor = ItemExtractor()
	val fetcherService = FetcherService()

	//val url = "https://www.kleinanzeigen.de/s-fahrraeder/herren/12309/seite:2/rennrad/k0c217l3411r10+fahrraeder.art_s:herren"
	url =


	val url = File("src/test/resources/rennraeder20260319.htm")
            .readText()

	val items = itemExtractor.extract(fetcherService.fetch(url))

	items.forEach {println(it)}
}
