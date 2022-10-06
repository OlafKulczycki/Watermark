fun main() {
    val input = readln()
    var firstHalf = 0
    var secondHalf = 0
    for (i in 0 until input.length / 2) {
        firstHalf += input[i].digitToInt()
    }
    for (i in input.length / 2 until input.length) {
        secondHalf += input[i].digitToInt()
    }
    if (firstHalf == secondHalf) println("YES") else println("NO")
}