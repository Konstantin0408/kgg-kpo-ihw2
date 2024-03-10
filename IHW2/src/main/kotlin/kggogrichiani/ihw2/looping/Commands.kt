package kggogrichiani.ihw2.looping

class HelpCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "help"
    override fun description(): String = "display all available commands"
    override fun execute() = looper.help()
}

class QuitCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "quit"
    override fun description(): String = "exit the app (all data is saved)"
    override fun execute() = looper.quit()
}

class ViewDishesCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "menu"
    override fun description(): String = "view all dishes in the menu"
    override fun execute() = looper.viewDishes()
}

class WatchReviewsCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "watch_reviews"
    override fun description(): String = "view all reviews to a dish"
    override fun execute() = looper.watchReviews()
}

class RegisterCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "register"
    override fun description(): String = "create new account"
    override fun execute() = looper.register()
}

class LoginCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "log"
    override fun description(): String = "log in to an existing account"
    override fun execute() = looper.login()
}

class LogoutCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "logout"
    override fun description(): String = "log out"
    override fun execute() = looper.logout()
}

class CreateOrderCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "create_order"
    override fun description(): String = "create new order (and enter it)"
    override fun execute() = looper.createOrder()
}

class SelectOrderCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "select_order"
    override fun description(): String = "select order by ID (from the list)"
    override fun execute() = looper.selectOrder()
}

class FillCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "fill"
    override fun description(): String = "add money to account"
    override fun execute() = looper.fill()
}

class CheckCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "c"
    override fun description(): String = "check if any orders are ready"
    override fun execute() = looper.check()
}

class ViewOrdersCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "view_orders"
    override fun description(): String = "view all orders and their status"
    override fun execute() = looper.viewOrders()
}

class StartOrderCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "start_order"
    override fun description(): String = "start ordering dishes"
    override fun execute() = looper.startOrder()
}

class FinishOrderCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "finish_order"
    override fun description(): String = "complete the order and start waiting its delivery"
    override fun execute() = looper.finishOrder()
}

class ExitOrderCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "exit_order"
    override fun description(): String = "stop forming current order"
    override fun execute() = looper.exitOrder()
}

class ViewOrderCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "view_order"
    override fun description(): String = "view all dishes in this order"
    override fun execute() = looper.viewOrder()
}

class CancelOrderCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "cancel"
    override fun description(): String = "cancel order (must be done before delivery)"
    override fun execute() = looper.cancelOrder()
}

class PayCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "pay"
    override fun description(): String = "pay for order (once delivered)"
    override fun execute() = looper.pay()
}

class AdminAddDishCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "add_dish"
    override fun description(): String = "add a new dish to the menu"
    override fun execute() = looper.adminAddDish()
}

class AdminChangeDishCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "change_dish"
    override fun description(): String = "change cost & difficulty of an existing dish"
    override fun execute() = looper.adminChangeDish()
}

class AdminRemoveDishCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "remove_dish"
    override fun description(): String = "remove dish from the menu"
    override fun execute() = looper.adminRemoveDish()
}

class AdminStatsCommand(looper: Looper) : LoopingCommand(looper) {
    override fun name(): String = "stats"
    override fun description(): String = "view statistics"
    override fun execute() = looper.adminStats()
}
