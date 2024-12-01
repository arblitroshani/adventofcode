package util.common

import java.awt.geom.Point2D

data class Line(val slope: Double, val b: Double) {
    fun intersectionPointTo(other: Line): Point2D.Double? {
        val delta = this.slope - other.slope
        if (delta == 0.0) return null // lines are parallel

        val x = (other.b - this.b) / delta
        val y = this.slope * x + this.b
        return Point2D.Double(x, y)
    }

    companion object {
        fun from(vector: Vector3D): Line {
            val slope = vector.vy.toDouble() / vector.vx
            val b = vector.index.y - slope * vector.index.x
            return Line(slope, b)
        }
    }
}
