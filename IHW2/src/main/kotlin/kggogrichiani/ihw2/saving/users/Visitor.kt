package kggogrichiani.ihw2.saving.users

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kggogrichiani.ihw2.saving.orders.Order
import kggogrichiani.ihw2.saving.menu.Menu

@Serializable
class Visitor(
    @Transient private val visitorUsername: String = "#",
    @Transient private val visitorPassword: String = "#",
    var balance: Int = 0
) : User(visitorUsername, visitorPassword) {
    var orders: MutableList<Order> = mutableListOf()

    fun constructOrders(menu: Menu) = orders.forEach { order -> order.constructDishes(this, menu) }

    fun createNewOrder() : Order {
        val order = Order()
        orders.add(order)
        return order
    }

    fun displayOrders() {
        orders.forEachIndexed { i: Int, order: Order ->
            println("ID: $i. ${order.description()}, state: ${order.status}")
        }
    }

    fun addMoney(amount: Int) {
        balance += amount
    }
    fun payMoney(amount: Int) {
        balance -= amount
    }
}
