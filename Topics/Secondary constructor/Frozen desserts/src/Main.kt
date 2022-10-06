class IceCreamOrder () {
    var price: Int = 0
    constructor(popsicles: Int) : this() {
        this.price = popsicles * 7
    }
    constructor(scoops: Int, toppings: Int) : this() {
        this.price = scoops * 5 + 2 * toppings
    }
}