class Intern(val weeklyWorkload: Int) {
    val baseWorkload = 20

    inner class Salary {
        val basePay = 50
        val extraHoursPay = 2.8
    }

    val weeklySalary = if (weeklyWorkload > baseWorkload) {
        ((weeklyWorkload - 20) * Intern(weeklyWorkload).Salary().extraHoursPay) + 50
    } else
        Intern(weeklyWorkload).Salary().basePay
}
fun main() {
    println(Intern(30).weeklySalary)
}