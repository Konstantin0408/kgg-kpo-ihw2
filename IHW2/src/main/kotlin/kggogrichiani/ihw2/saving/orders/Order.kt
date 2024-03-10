package kggogrichiani.ihw2.saving.orders

import kggogrichiani.ihw2.saving.dishes.Dish
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kggogrichiani.ihw2.saving.menu.Menu
import kggogrichiani.ihw2.saving.users.Visitor
import kotlin.concurrent.thread

@Serializable
class Order(
    private val dishNames: MutableList<String> = mutableListOf(),
) {
    private var completion: Int = 0
    var status = OrderStatus.IN_ORDERING
    @Transient private var visitor: Visitor? = null
    @Transient var dishes = mutableListOf<Dish>()

    fun constructDishes(visitor: Visitor, menu: Menu) {
        this.visitor = visitor
        dishes = (dishNames.map { dishName -> menu.getDishByName(dishName) as Dish }).toMutableList()
        if (status == OrderStatus.AWAITING_PREPARATION || status == OrderStatus.IN_PREPARATION) handleBySecond()
    }

    fun withDish(dish: Dish): Order {
        dishes.add(dish)
        dishNames.add(dish.name)
        return this
    }

    private fun dishCount() = dishes.size
    fun totalCost() = dishes.sumOf { dish -> dish.cost }
    fun description() : String {
        if (dishes.isEmpty()) return "empty order"
        return "${dishCount()} dishes ordered, $${totalCost()}"
    }

    private fun handleBySecond() {
        thread(start = true) {
            while (completion > 0) {
                Thread.sleep(1000)
                completion--
            }
            status = OrderStatus.READY
        }
    }
    fun handle() {
        status = OrderStatus.IN_PREPARATION
        completion = dishes.sumOf { dish -> dish.difficulty }
        handleBySecond()
    }
    fun cancel() {
        status = OrderStatus.CANCELLED
    }

    override fun toString(): String = dishes.fold("") { acc: String, dish: Dish ->
        if (acc == "") dish.toString() else "$acc; $dish"
    }
}
