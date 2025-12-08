package util.common

import java.awt.geom.Point2D

data class Point3D(val x: Long, val y: Long, val z: Long)

data class Vector3D(
    val index: Point3D,
    val vx: Int,
    val vy: Int,
    val vz: Int,
) {
    val line: Line by lazy { Line.from(this) }

    fun isInFuture(intersection: Point2D.Double): Boolean {
        if (vy < 0 && intersection.y >= index.y) return false
        if (vy > 0 && intersection.y <= index.y) return false
        return true
    }
}

fun Line.Companion.from(vector: Vector3D): Line {
    val slope = vector.vy.toDouble() / vector.vx
    val b = vector.index.y - slope * vector.index.x
    return Line(slope, b)
}
