package me.dylancurzon.nea.util;

import com.sun.istack.internal.NotNull;

public class Vector3d {

    @NotNull
    protected final double x;
    @NotNull
    protected final double y;
    @NotNull
    protected final double z;

    public Vector3d(@NotNull final double x, @NotNull final double y, @NotNull final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3d of(@NotNull final double x, @NotNull final double y,
                              @NotNull final double z) {
        return new Vector3d(x, y, z);
    }

    @NotNull
    public Vector3d add(@NotNull final Vector3d addend) {
        return new Vector3d(
            this.x + addend.getX(),
            this.y + addend.getY(),
            this.z + addend.getZ()
        );
    }

    @NotNull
    public Vector3d add(@NotNull final double addend) {
        return new Vector3d(
            this.x + addend,
            this.y + addend,
            this.z + addend
        );
    }

    @NotNull
    public Vector3d sub(@NotNull final Vector3d subtrahend) {
        return new Vector3d(
            this.x - subtrahend.getX(),
            this.y - subtrahend.getY(),
            this.z - subtrahend.getZ()
        );
    }

    @NotNull
    public Vector3d sub(@NotNull final double subtrahend) {
        return new Vector3d(
            this.x - subtrahend,
            this.y - subtrahend,
            this.z - subtrahend
        );
    }

    @NotNull
    public Vector3d mul(@NotNull final Vector3d factor) {
        return new Vector3d(
            this.x * factor.getX(),
            this.y * factor.getY(),
            this.z * factor.getZ()
        );
    }

    @NotNull
    public Vector3d mul(@NotNull final double factor) {
        return new Vector3d(
            this.x * factor,
            this.y * factor,
            this.z * factor
        );
    }

    @NotNull
    public Vector3d div(@NotNull final Vector3d divisor) {
        return new Vector3d(
            this.x / divisor.getX(),
            this.y / divisor.getY(),
            this.z / divisor.getZ()
        );
    }

    @NotNull
    public Vector3d div(@NotNull final double divisor) {
        return new Vector3d(
            this.x / divisor,
            this.y / divisor,
            this.z / divisor
        );
    }

    @NotNull
    public Vector3d floor() {
        return new Vector3d(
            Math.floor(this.x),
            Math.floor(this.y),
            Math.floor(this.z)
        );
    }

    /**
     * @return Vector with each component rounded to the nearest integer which is the closest to
     * zero.
     */
    @NotNull
    public Vector3d floorAbs() {
        return new Vector3d(
            this.x >= 0 ? Math.floor(this.x) : Math.ceil(this.x),
            this.y >= 0 ? Math.floor(this.y) : Math.ceil(this.y),
            this.z >= 0 ? Math.floor(this.z) : Math.ceil(this.z)
        );
    }

    @NotNull
    public Vector3d ceil() {
        return new Vector3d(
            Math.ceil(this.x),
            Math.ceil(this.y),
            Math.ceil(this.z)
        );
    }

    /**
     * @return Vector with each component rounded to the nearest integer which is furthest away
     * from zero.
     */
    @NotNull
    public Vector3d ceilAbs() {
        return new Vector3d(
            this.x >= 0 ? Math.ceil(this.x) : Math.floor(this.x),
            this.y >= 0 ? Math.ceil(this.y) : Math.floor(this.y),
            this.z >= 0 ? Math.ceil(this.z) : Math.floor(this.z)
        );
    }

    @NotNull
    public double abs() {
        return Math.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z));
    }

//    @NotNull
//    public Vector2i toInt() {
//        return new Vector2i(
//            (int) this.x,
//            (int) this.y
//        );
//    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof Vector3d) {
            final Vector3d vector = (Vector3d) object;
            return this.x == vector.getX() && this.y == vector.getY() && this.z == vector.getZ();
        }
        return super.equals(object);
    }

//    @Override
//    public int hashCode() {
//        return (int) Math.pow(Math.pow(this.x * 0x1f1f1f1f, this.y), this.z);
//    }

    @Override
    public String toString() {
        return String.format("(%f, %f, %f)", this.x, this.y, this.z);
    }

}