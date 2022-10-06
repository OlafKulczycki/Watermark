val lambda: (Long, Long) -> Long = { first, second -> (first..second).reduce { first, second -> first * second } }
