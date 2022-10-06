class Vehicle(val name: String) {

    inner class Body(val color: String) {
        fun printColor() = println("The $name vehicle has a $color body.")
    }
}
