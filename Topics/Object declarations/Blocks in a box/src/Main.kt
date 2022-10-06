class Block(val color: String) {
    object BlockProperties {
        var length = 6
        var width = 4
        fun blocksInBox(length: Int, width: Int): Int = length / this.length * (width / this.width)
    }
}