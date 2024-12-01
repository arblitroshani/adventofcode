package util.common

enum class Dir {
    U, D, L, R;

    val isVertical: Boolean get() = this == U || this == D

    // turn 180 degrees
    val opposite: Dir get() = when(this) {
        U -> D
        D -> U
        L -> R
        R -> L
    }

    // turn clockwise 90 degrees
    val cw: Dir get() = when(this) {
        U -> R
        R -> D
        D -> L
        L -> U
    }

    // turn counter-clockwise 90 degrees
    val ccw: Dir get() = this.cw.opposite
}
