fun main() {
    val input = readln().toString()
    var x = 0
    val j = input.length
    for (i in input.length - 1 downTo 0) {
        if (input[i] == 'c' || input[i] == 'g' || input[i] == 'C' || input[i] == 'G') {
            x++
        }
    }
    println(x.toDouble() / j.toDouble() * 100)
}
