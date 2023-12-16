package com.arblitroshani.adventofcode.util.common

enum class Dir {
    U, D, L, R;

    private val opposite: Dir get() = when(this) {
        U -> D
        D -> U
        L -> R
        R -> L
    }

    val isVertical: Boolean get() = this == U || this == D

    val nextCw: Dir get() = when(this) {
        U -> R
        R -> D
        D -> L
        L -> U
    }

    val nextCcw: Dir get() = this.nextCw.opposite
}
