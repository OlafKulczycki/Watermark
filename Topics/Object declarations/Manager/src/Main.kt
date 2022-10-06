class Task(val name: String)

object Manager {
    var solvedTask = 0
    fun solveTask(whatever: Task) {
        println("Task ${whatever.name} solved!")
        solvedTask++
    }
}