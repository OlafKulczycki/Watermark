fun main() {
    val input = readln()
    val x = input.length / 2
    if (input.length % 2 == 0) {
        print(input.substring(0, x - 1))
        print(input.substring(x + 1))
    } else {
        print(input.substring(0, x))
        print(input.substring(x + 1))
    }
}