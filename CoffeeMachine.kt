package machine
import machine.coffee.*

data class CoffeeMachine(private var water: Int, private var milk: Int, private var beans: Int, private var cups: Int, private var money: Int) {

    fun start() {
        this.state = MachineState.WAITING_ACTION
    }

    fun stop() {
        this.state = MachineState.SWITCHED_OFF
    }

    enum class MachineState(val value: String) {
        WAITING_ACTION("waiting-action"),
        WAITING_COFFEE_VARIANT("waiting-coffee-variant"),
        SWITCHED_OFF("switched-off")
    }

    enum class MachineAction(val id: String) {
        BUY("buy"),
        FILL("fill"),
        TAKE("take"),
        REMAINING("remaining"),
        EXIT("exit"),
        BACK("back")
    }

    var state: MachineState = MachineState.SWITCHED_OFF

    val coffees: String
        get() {
            var result = ""
            for (type in CoffeeType.values()) {
                val comma = if (type.ordinal == CoffeeType.values().size - 1) "" else ","
                result += "${type.id} - ${type.toString().toLowerCase()}$comma "
            }

            return result.trimEnd()
        }

    fun handleBuy(value: String) {
        this.state = MachineState.WAITING_COFFEE_VARIANT

        val coffeeId = value.trim().toInt()
        val (coffee, resources) = makeCoffee(CoffeeType.values().find { it.id == coffeeId } as CoffeeType)

        if (coffee != null) {
            println("I have enough resources making you a coffee!\n")
            this.countExpenses(coffee)
        } else {
            var output = ""
            for ((key, value) in resources.entries) {
                if (!value) {
                    output += "Sorry, not enough $key!\n"
                }
            }
            println(output)
        }

        this.start()
    }

    private fun makeCoffee(coffeeId: CoffeeType): Pair<Coffee?, Map<String, Boolean>> {
        when (coffeeId) {
            CoffeeType.ESPRESSO -> {
                val prepare = Espresso()
                val resources = countResources(prepare)
                val coffee = if (resources.get("water")!! && resources.get("beans")!! && resources.get("cups")!!) {
                    prepare
                } else null

                return Pair(coffee, resources)
            }
            CoffeeType.LATTE -> {
                val prepare = Latte()
                val resources = countResources(prepare)
                val coffee = if (resources.get("water")!! && resources.get("beans")!! && resources.get("milk")!! && resources.get("cups")!!) {
                    prepare
                } else null

                return Pair(coffee, resources)
            }
            CoffeeType.CAPPUCCINO -> {
                val prepare = Cappuccino()
                val resources = countResources(prepare)
                val coffee = if (resources.get("water")!! && resources.get("beans")!! && resources.get("milk")!! && resources.get("cups")!!) {
                    prepare
                } else null

                return Pair(coffee, resources)
            }
        }
    }

    private fun countResources(coffee: Coffee): Map<String, Boolean> {
        return mapOf<String, Boolean>(
            "water" to (this.water >= coffee.water),
            "beans" to (this.beans >= coffee.beans),
            "milk" to (this.milk >= coffee.milk),
            "cups" to (this.cups > 0)
        )
    }

    private fun countExpenses(coffee: Coffee) {
        this.cups -= 1
        this.water -= coffee.water
        this.milk -= coffee.milk
        this.beans -= coffee.beans
        this.money += coffee.cost
    }

    fun handleFill(waterToAdd: Int, milkToAdd: Int, beansToAdd: Int, cupsToAdd: Int) {
        this.water += waterToAdd
        this.milk += milkToAdd
        this.beans += beansToAdd
        this.cups += cupsToAdd

        this.start()
    }

    fun handleTake() {
        val money = this.money
        this.money = 0
        println("I gave you \$$money\n")

        this.start()
    }

    fun handleRemaining() {
        println(this.getInfo() + '\n')

        this.start()
    }

    private fun getInfo(): String {
        return (
            """
            The coffee machine has:
            $water of water
            $milk of milk
            $beans of coffee beans
            $cups of disposable cups
            $money of money
            """.trimIndent()
        )
    }
}
