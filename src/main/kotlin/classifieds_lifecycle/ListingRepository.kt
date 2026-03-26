package classifieds_lifecycle

class ListingRepository {
        val jdbcUrl = "jdbc:postgresql://localhost:5432/kleinanzeigen"

    val connection = DriverManager.getConnection(jdbcUrl, "postgres", "fennpfuhl")


    val allItems = paginator.run(config)
    allItems.forEach { item ->

        val query = connection.prepareStatement("INSERT INTO listings (id, title, price, negotiable, created) VALUES (?,?,?,?,?) ON CONFLICT (id) DO NOTHING;") // DO UPDATE when updating row with new data
        query.setLong(1, item.id.toLong())
        query.setString(2, item.title)
        query.setBigDecimal(3, item.price.toBigDecimal())
        query.setBoolean(4, item.negotiable)
        if (item.created != null) query.setDate(5, Date.valueOf(item.created))
        else query.setNull(5, Types.DATE)
        query.executeUpdate()

        println("$item")

    }
}