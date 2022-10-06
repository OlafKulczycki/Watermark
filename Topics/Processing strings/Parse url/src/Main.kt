fun main() {
    var input = readln()
    if (input.contains("pass")) {
        input = input.replace("&", "").split("=").toString()
        println("")
    }
}