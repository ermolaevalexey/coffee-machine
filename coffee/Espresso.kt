package machine.coffee

class Espresso: Coffee(250, 0, 16, 4) {
    override val type = CoffeeType.ESPRESSO
}
