package machine

import java.util.*

fun main() {
    val machine = CoffeeMachine(400, 540, 120, 9, 550)
    val scanner = Scanner(System.`in`)
    machine.start()

    while (machine.state == CoffeeMachine.MachineState.WAITING_ACTION) {
        println("Write action (buy, fill, take, remaining, exit):")
        val action = scanner.nextLine()

        when (action) {
            CoffeeMachine.MachineAction.BUY.id -> {
                println("What do you want to buy? ${machine.coffees}")
                val v = scanner.next()
                if (v.trim() != CoffeeMachine.MachineAction.BACK.id) {
                    machine.handleBuy(v)
                } else {
                    machine.start()
                }
                scanner.nextLine()
            }
            CoffeeMachine.MachineAction.FILL.id -> {
                println("Write how many ml of water do you want to add: ")
                val waterToAdd = scanner.nextInt()
                println("Write how many ml of milk do you want to add: ")
                val milkToAdd = scanner.nextInt()
                println("Write how many grams of coffee beans do you want to add: ")
                val beansToAdd = scanner.nextInt()
                println("Write how many disposable cups of coffee do you want to add: ")
                val cupsToAdd = scanner.nextInt()
                machine.handleFill(waterToAdd, milkToAdd, beansToAdd, cupsToAdd)
            }
            CoffeeMachine.MachineAction.TAKE.id -> machine.handleTake()
            CoffeeMachine.MachineAction.REMAINING.id -> machine.handleRemaining()
            CoffeeMachine.MachineAction.EXIT.id -> machine.stop()
        }
    }
}
