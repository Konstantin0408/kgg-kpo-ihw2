package kggogrichiani.ihw2.saving.dishes

import kotlinx.serialization.Serializable

@Serializable
class Review(val visitorName: String, val rating: Int, private val comment: String) {
    override fun toString(): String {
        return "($rating stars) $visitorName says:\n$comment"
    }
}
