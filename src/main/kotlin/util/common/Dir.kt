package util.common

enum class Dir {
    U, D, L, R, UR, DR, DL, UL;

    val isVertical: Boolean get() = this == U || this == D

    // turn 180 degrees
    val opposite: Dir get() = when(this) {
        U -> D
        D -> U
        L -> R
        R -> L
        UR -> DL
        DL -> UR
        DR -> UL
        UL -> DR
    }

    // turn clockwise 90 degrees
    val cw: Dir get() = when(this) {
        U -> R
        R -> D
        D -> L
        L -> U
        UR -> DR
        DR -> DL
        DL -> UL
        UL -> UR
    }

    // turn counter-clockwise 90 degrees
    val ccw: Dir get() = this.cw.opposite
}
