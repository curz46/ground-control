package me.dylancurzon.nea.util;

import com.sun.istack.internal.NotNull;

import java.util.Objects;

public class PolarCoord {

    private final double radius;
    private final float theta;
    private final float phi;

    private PolarCoord(final double radius, final float theta, final float phi) {
        this.radius = radius;
        this.theta = theta;
        this.phi = phi;
    }

    @NotNull
    public static PolarCoord of(final double radius, final float theta, final float phi) {
        return new PolarCoord(radius, theta, phi);
    }

    // http://tutorial.math.lamar.edu/Classes/CalcIII/SphericalCoords.aspx
    public Vector3d toVector3d() {
        return Vector3d.of(
            this.radius * Math.sin(this.phi) * Math.cos(this.theta),
            this.radius * Math.sin(this.phi) * Math.sin(this.theta),
            this.radius * Math.cos(this.phi)
        );
    }

    public double getRadius() {
        return this.radius;
    }

    public float getTheta() {
        return this.theta;
    }

    public float getPhi() {
        return this.phi;
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof PolarCoord)) return false;
        final PolarCoord coord = (PolarCoord) object;
        return this.radius == coord.getRadius()
            && this.theta == coord.getTheta()
            && this.phi == coord.getPhi();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.radius, this.theta, this.phi);
    }

    @Override
    public String toString() {
        return String.format("PolarCoord{%f,%f,%f}", this.radius, this.theta, this.phi);
    }
}
