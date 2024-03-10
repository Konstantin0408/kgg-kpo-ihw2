package kggogrichiani.ihw2.saving.menu

import kggogrichiani.ihw2.saving.dishes.Dish
import kotlinx.serialization.Serializable

@Serializable
class Menu(
    var dishes: MutableList<Dish> = mutableListOf()
) {
    fun addDish(dish: Dish) = dishes.add(dish)
    fun removeDish(dish: Dish) = dishes.remove(dish)
    fun dishCount() = dishes.size
    fun viewDishes() = dishes.forEachIndexed { index, dish -> println("$index. $dish") }
    fun getDish(id: Int) = dishes[id]
    fun getDishByName(name: String) = dishes.find { dish -> dish.name == name }
}
