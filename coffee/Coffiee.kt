package machine.coffee

abstract class Coffee(val water: Int, val milk: Int, val beans: Int, val cost: Int) {
    abstract val type: CoffeeType;
}
