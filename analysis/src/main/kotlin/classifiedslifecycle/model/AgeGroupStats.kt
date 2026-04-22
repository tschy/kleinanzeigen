package classifiedslifecycle.model

data class AgeGroupStats(
    val ageGroup: String,
    val count: Int,
    val countOnline: Int,
    val countOffline: Int,
    val percentageDiscount: Double?,
    val percentageDiscountOnline: Double?,
    val percentageDiscountOffline: Double?,
    val percentageOnline: Double?,
    val percentageOffline: Double?,
)