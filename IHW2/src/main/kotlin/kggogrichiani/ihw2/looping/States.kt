package kggogrichiani.ihw2.looping

import kggogrichiani.ihw2.saving.orders.Order
import kggogrichiani.ihw2.saving.users.Admin
import kggogrichiani.ihw2.saving.users.User
import kggogrichiani.ihw2.saving.users.Visitor

class Quit : LooperState {
    override fun description() = "quit"
    override fun getAllCommands(looper: Looper) = listOf<LoopingCommand>()
}

abstract class ConsoleState : LooperState {
    override fun getAllCommands(looper: Looper) = listOf(
        HelpCommand(looper), QuitCommand(looper),
        ViewDishesCommand(looper), WatchReviewsCommand(looper)
    )
}

class NotLoggedIn : ConsoleState() {
    override fun description() = "not logged in"
    override fun getAllCommands(looper: Looper) = super.getAllCommands(looper) + listOf(
        RegisterCommand(looper), LoginCommand(looper)
    )
}

abstract class LoggedIn(
    val user: User
) : ConsoleState() {
    abstract fun userType() : String
    override fun description() = "logged in as ${userType()} ${user.username}"
    override fun getAllCommands(looper: Looper) = super.getAllCommands(looper) + listOf(
        LogoutCommand(looper)
    )
}

abstract class LoggedInAsVisitor(
    visitor: Visitor
) : LoggedIn(visitor) {
    override fun userType(): String = "visitor"
    override fun description(): String = "${super.description()} [$${getVisitor().balance}]"
    fun getVisitor() = user as Visitor
    override fun getAllCommands(looper: Looper) = super.getAllCommands(looper) + listOf(
        CreateOrderCommand(looper), SelectOrderCommand(looper),
        FillCommand(looper), CheckCommand(looper)
    )
}

class LoggedInAsVisitorWithoutOrder(
    visitor: Visitor
) : LoggedInAsVisitor(visitor) {
    override fun getAllCommands(looper: Looper) = super.getAllCommands(looper) + listOf(
        ViewOrdersCommand(looper)
    )
}

class LoggedInAsVisitorWithOrder(
    visitor: Visitor,
    val order: Order
) : LoggedInAsVisitor(visitor) {
    override fun description(): String = "${super.description()}, ${order.description()}"
    override fun getAllCommands(looper: Looper) = super.getAllCommands(looper) + listOf(
        StartOrderCommand(looper), FinishOrderCommand(looper), ExitOrderCommand(looper),
        ViewOrderCommand(looper), CancelOrderCommand(looper), PayCommand(looper)
    )
}

class LoggedInAsAdmin(
    admin: Admin
) : LoggedIn(admin) {
    override fun userType(): String = "admin"
    override fun getAllCommands(looper: Looper) = super.getAllCommands(looper) + listOf(
        AdminAddDishCommand(looper), AdminChangeDishCommand(looper),
        AdminRemoveDishCommand(looper), AdminStatsCommand(looper)
    )
}