package kggogrichiani.ihw2.saving.dishes

import kotlinx.serialization.Serializable
import java.math.RoundingMode

@Serializable
class Dish(
    val name: String,
    var cost: Int,
    var difficulty: Int = 15
) {
    var orderCount: Int = 0
    val reviews: MutableList<Review> = mutableListOf()

    fun addReview(review: Review) = reviews.add(review)
    fun averageRating() : Double {
        if (reviews.isEmpty()) return 0.0
        return (reviews.sumOf { review -> review.rating.toDouble() } / reviews.size)
            .toBigDecimal().setScale(1, RoundingMode.HALF_UP).toDouble()
    }

    override fun toString(): String =
        if (reviews.isEmpty()) "$name ($$cost)" else "$name ($$cost, ${averageRating()}*)"
}
