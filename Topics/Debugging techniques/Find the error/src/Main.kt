import jdk.internal.vm.compiler.word.LocationIdentity.init
import java.util.Scanner

fun swapInts(ints: IntArray): IntArray {
    return intArrayOf(ints[1], ints[0])
}

fun main() {
    val scanner = Scanner(System.`in`)
    while (scanner.hasNextLine()) {
        var ints = intArrayOf(
            scanner.nextLine().toInt(),
            scanner.nextLine().toInt(),
        )
        println(swapInts(ints).first())
        println(swapInts(ints).last())
    }
    var retainList = ArrayList<String>()
    retainList.add("Apple")
    retainList.retainAll()

}