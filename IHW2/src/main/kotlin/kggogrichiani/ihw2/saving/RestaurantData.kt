package kggogrichiani.ihw2.saving

import kotlinx.serialization.Serializable
import kggogrichiani.ihw2.saving.menu.Menu
import kggogrichiani.ihw2.saving.users.User

@Serializable
class RestaurantData(
    val users: MutableList<User>,
    val menu: Menu = Menu(),
    var totalEarnings: Int = 0
) {
    companion object {
        val empty = RestaurantData(mutableListOf(), Menu())
    }
}