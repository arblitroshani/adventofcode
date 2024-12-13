package util.common

enum class Dir {
    U, D, L, R, UR, DR, DL, UL;

    val isVertical: Boolean get() = this == U || this == D

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
    val ccw: Dir get() = this.cw.cw.cw
}
