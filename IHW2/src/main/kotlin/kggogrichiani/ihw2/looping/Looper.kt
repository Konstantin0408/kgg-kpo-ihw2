package kggogrichiani.ihw2.looping
import kggogrichiani.ihw2.saving.dishes.Dish
import kggogrichiani.ihw2.saving.orders.OrderStatus
import kggogrichiani.ihw2.saving.RestaurantData
import kggogrichiani.ihw2.saving.dishes.Review
import kggogrichiani.ihw2.saving.orders.Order
import kggogrichiani.ihw2.saving.users.Admin
import kggogrichiani.ihw2.saving.users.Visitor
import java.io.File
import java.util.Scanner

class Looper(
    private val scanner: Scanner,
    private val save: RestaurantData,
    private var saveFile: File
) {
    private var state: LooperState = NotLoggedIn()
    private val users = save.users
    private val menu = save.menu

    private fun initialize() {
        users.forEach { user -> if (user is Visitor) user.constructOrders(menu) }

        println("Enter 'help' at any point to see list of available commands. " +
                "Enter 'register' to create a new account. " +
                "Enter 'log' to log in to an existing account.")
    }

    private fun getAllCommands() = state.getAllCommands(this)

    internal fun help() {
        val commands = getAllCommands()
        if (commands.isEmpty()) {
            println("???")
            return
        }
        println("Commands:")
        commands.forEach { loopingCommand ->
            println("${loopingCommand.name()}: ${loopingCommand.description()}")
        }
    }

    private fun saveData() = ScanningUtil.saveToFile(save, saveFile)

    internal fun quit() {
        state = Quit()
    }

    private fun getOrder() = (state as LoggedInAsVisitorWithOrder).order
    private fun getVisitor() = (state as LoggedInAsVisitor).getVisitor()

    internal fun register() {
        val username = ScanningUtil.getLine(scanner, "Username: ")
        if (users.find { user -> user.username == username } != null) {
            println("User with this username already exists")
            return
        }
        val password = ScanningUtil.getLine(scanner, "Password: ")
        val type = ScanningUtil.getLine(
            scanner,
            "Enter 'v' to create visitor or 'a' to create admin: "
        )
        when (type) {
            "v" -> {
                users.add(Visitor(username, password))
                println("Added visitor \"$username\"")
            }
            "a" -> {
                users.add(Admin(username, password))
                println("Added admin \"$username\"")
            }

            else -> {
                println("Unknown type")
                return
            }
        }
    }

    internal fun viewDishes() = menu.viewDishes()

    internal fun startOrder() {
        val order = getOrder()
        if (order.status != OrderStatus.IN_ORDERING) {
            println("This order is already finished")
            return
        }
        viewDishes()
        println("Start ordering dishes by ID. Enter '#' to stop ordering.")
        var str: String
        while (true) {
            str = ScanningUtil.getLine(scanner, "Dish ID: ")
            if (str == "#") break
            val dishID = ScanningUtil.safeStrToInt(str)
            if (dishID == null || dishID < 0 || dishID >= menu.dishCount()) {
                println("Invalid ID, try again")
                continue
            }
            val dish = menu.getDish(dishID)
            order.withDish(dish)
            println("Added dish ${dish.name} to order")
        }
    }

    internal fun login() {
        val username = ScanningUtil.getLine(scanner, "Username: ")
        val user = users.find { user -> user.username == username }
        if (user == null) {
            println("User with such username does not exist")
            return
        }
        val password = ScanningUtil.getLine(scanner, "Password: ")
        if (user.password != password) {
            println("Incorrect password")
            return
        }
        when (user) {
            is Visitor -> {
                state = LoggedInAsVisitorWithoutOrder(user)
                println("Logged in as visitor $username")
            }
            is Admin -> {
                state = LoggedInAsAdmin(user)
                println("Logged in as admin $username")
            }
            else -> {
                println("Unknown user type")
                // this is in case any more user types are added
            }
        }
    }

    internal fun watchReviews() {
        viewDishes()
        val dishID = ScanningUtil.getInt(scanner, "Enter dish ID: ")
        if (dishID == null || dishID < 0 || dishID >= menu.dishCount()) {
            println("Invalid dish ID")
            return
        }
        val dish = menu.getDish(dishID)
        dish.reviews.forEach { review -> print(review) }
    }

    internal fun createOrder() {
        val user = getVisitor()
        val newOrder = user.createNewOrder()
        state = LoggedInAsVisitorWithOrder(user, newOrder)
        println("Entered new order (enter 'start_order' to start ordering dishes)")
    }

    internal fun viewOrders() = getVisitor().displayOrders()

    internal fun finishOrder() {
        if (state is LoggedInAsVisitorWithOrder) {
            val user = getVisitor()
            val order = getOrder()
            if (order.status != OrderStatus.IN_ORDERING) {
                println("This order is already finished")
                return
            }
            order.status = OrderStatus.AWAITING_PREPARATION
            order.handle()
            state = LoggedInAsVisitorWithoutOrder(user)
            println("Finished ordering")
        }
    }

    internal fun exitOrder() {
        val user = getVisitor()
        state = LoggedInAsVisitorWithoutOrder(user)
        println("Exited current order")
    }

    internal fun check() {
        val user = getVisitor()
        val ready = user.orders.filter { order -> order.status == OrderStatus.READY }
        if (ready.isEmpty()) println("No orders ready yet")
        else println("${ready.size} order(s) ready!")
    }

    internal fun selectOrder() {
        viewOrders()
        val i = ScanningUtil.getInt(scanner, "Enter order ID: ")
        val user = getVisitor()
        if (i == null || i < 0 || i >= user.orders.size) {
            println("Invalid order ID")
            return
        }
        state = LoggedInAsVisitorWithOrder(user, user.orders[i])
        println("Entered order #$i")
    }

    internal fun viewOrder() {
        val order = getOrder()
        println("${order.description()}, ${order.status}")
        println(order)
    }

    internal fun cancelOrder() {
        val order = getOrder()
        if (order.status == OrderStatus.READY || order.status == OrderStatus.PAID_FOR) {
            println("You cannot cancel an order that has already been delivered")
            return
        }
        val user = getVisitor()
        order.cancel()
        user.orders.remove(order)
        state = LoggedInAsVisitorWithoutOrder(user)
        println("Order cancelled")
    }

    internal fun fill() {
        val user = getVisitor()
        val amount = ScanningUtil.getInt(scanner, "Enter amount: ")
        if (amount == null || amount <= 0) {
            println("Invalid amount")
            return
        }
        user.addMoney(amount)
        println("You added $$amount to your account, you now have $${user.balance}")
    }

    private fun reviewDishes(order: Order) {
        val unreviewedDishes = order.dishes.filter { dish ->
            dish.reviews.find { review -> review.visitorName == getVisitor().username } == null
        }
        unreviewedDishes.forEach { dish ->
            val rev = ScanningUtil.getYN(scanner, "Do you wish to review '${dish.name}'? (y/n) ")
            if (rev) {
                var rating = ScanningUtil.getInt(scanner, "Enter rating on the scale of 1-5: ")
                while (rating == null || rating < 1 || rating > 5) {
                    rating = ScanningUtil.getInt(scanner, "Invalid rating, try again: ")
                }

                var fullComment = ""
                println("Enter the comment line by line. Enter '#' to finish the comment, enter '%' to reset it")
                while (true) {
                    val line = scanner.nextLine()
                    if (line == "#") break
                    if (line == "%") {
                        fullComment = ""
                        println("(comment reset)")
                    } else fullComment += "$line\n"
                }

                dish.addReview(Review(getVisitor().username, rating, fullComment))
            }
        }
    }

    internal fun pay() {
        val order = getOrder()
        if (order.status != OrderStatus.READY) {
            println("You can only pay when the order is delivered")
            return
        }
        val user = getVisitor()
        val cost = order.totalCost()
        if (cost > user.balance) {
            println("You do not have enough money. Enter 'fill' to add money to your account")
            return
        }

        user.payMoney(cost)
        save.totalEarnings += cost
        order.status = OrderStatus.PAID_FOR
        state = LoggedInAsVisitorWithoutOrder(user)
        println("Payment successful, you have $${user.balance} remaining")
        val yesAddReview = ScanningUtil.getYN(scanner, "Do you wish to review the dishes? (y/n) ")
        if (yesAddReview) reviewDishes(order)
        user.orders.remove(order)
    }

    internal fun logout() {
        state = NotLoggedIn()
        println("Logged out")
    }

    internal fun adminAddDish() {
        val dishName = ScanningUtil.getLine(scanner, "Enter dish name: ")
        if (menu.dishes.find { dish -> dish.name == dishName } != null) {
            println("Dish with this name already exists")
            return
        }

        val dishCost = ScanningUtil.getInt(scanner, "Enter dish cost: ")
        if (dishCost == null || dishCost <= 0) {
            println("Invalid cost")
            return
        }

        val dishDifficulty = ScanningUtil.getInt(scanner, "Enter dish difficulty: ")
        if (dishDifficulty == null || dishDifficulty  <= 0) {
            println("Invalid difficulty")
            return
        }
        val dish = Dish(dishName, dishCost, dishDifficulty)
        menu.addDish(dish)

        println("Added dish \"$dishName\"")
    }

    internal fun adminChangeDish() {
        val dishName = ScanningUtil.getLine(scanner, "Enter dish name: ")
        val dish = menu.dishes.find { dish -> dish.name == dishName }
        if (dish == null) {
            println("Dish with such name does not exist")
            return
        }
        val dishCost = ScanningUtil.getInt(scanner, "Enter new cost: ")
        if (dishCost == null || dishCost <= 0) {
            println("Invalid cost")
            return
        }
        val dishDifficulty = ScanningUtil.getInt(scanner, "Enter new difficulty: ")
        if (dishDifficulty == null || dishDifficulty  <= 0) {
            println("Invalid difficulty")
            return
        }
        dish.cost = dishCost
        dish.difficulty = dishDifficulty
        println("Modified dish \"$dishName\"")
    }

    internal fun adminRemoveDish() {
        val dishName = ScanningUtil.getLine(scanner, "Enter dish name: ")
        val dish = menu.dishes.find { dish -> dish.name == dishName }
        if (dish == null) {
            println("Dish with such name does not exist")
            return
        }
        menu.removeDish(dish)
        println("Removed dish \"$dishName\"")
    }

    internal fun adminStats() {
        println("Highest ranking dishes:")
        val sortedByRating = menu.dishes.sortedByDescending { dish -> dish.averageRating() }
        sortedByRating.forEachIndexed { index, dish ->
            if (index < 3 && dish.averageRating() > 0) println(dish)
        }

        println("Most popular dishes:")
        val sortedByPopularity = menu.dishes.sortedByDescending { dish -> dish.orderCount }
        sortedByPopularity.forEachIndexed { index, dish ->
            if (index < 3 && dish.orderCount > 0) println(dish)
        }

        println("Total earnings: $${save.totalEarnings}")
    }

    fun run() {
        initialize()
        while (state !is Quit) {
            val str = ScanningUtil.getLine(scanner, "(${state.description()}) Command > ")
            val commands = getAllCommands()
            val command = commands.find { command -> command.name() == str }
            if (command == null) {
                println("Command not found. Enter 'help' to view all commands")
            } else {
                command.execute()
            }
            saveData()
        }
    }
}