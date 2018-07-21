package me.dylancurzon.nea;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.Renderable;
import me.dylancurzon.nea.util.Vector3d;

public class Planet implements Renderable {

    private static Vector3d eyePosition = Vector3d.of(-9.1, 0, 0);
    private static Vector3d viewPositionA = Vector3d.of(-9, 20, 20);
    private static Vector3d viewPositionB = Vector3d.of(-9, -20, -20);
    private final Vector3d sphereCentre = Vector3d.of(0, 0, 0);
    private final double radius = 5;

    public static void move(final Vector3d delta) {
        eyePosition = eyePosition.add(delta);
        viewPositionA = viewPositionA.add(delta);
        viewPositionB = viewPositionB.add(delta);
    }

    @Override
    public void render(final PixelContainer ctr) {
        final Vector3d pointA = this.viewPositionA;
        final Vector3d pointB = this.viewPositionB;
        for (int x = 0; x < ctr.getWidth(); x++) {
            for (int y = 0; y < ctr.getHeight(); y++) {
                // Using linear interpolation, find position of the pixel centre.
                final Vector3d pixelPointA = pointA.add(
                    pointB.sub(pointA).mul(
                        Vector3d.of(
                            0,
                            ((double) y) / ctr.getHeight(),
                            ((double) x) / ctr.getWidth()
                        )
                    )
                );
                final Set<Vector3d> intersections = this.findIntersections(
                    this.eyePosition,
                    pixelPointA
                );
                if (intersections.size() != 2) {
                    ctr.setPixel(x, y, 0xFFFFFFFF);
                    continue;
                }
//                ctr.setPixel(x, y, 0xFFFF0000);
                Vector3d closest = null;
                for (final Vector3d vector : intersections) {
                    if (closest == null) {
                        closest = vector;
                        continue;
                    }
                    final double dist = this.eyePosition.sub(closest).abs();
                    if (dist > this.eyePosition.sub(vector).abs()) {
                        closest = vector;
                    }
                }
                final double dist = this.eyePosition.sub(closest).abs();
                final int g = (int) (100 + (dist / 3 * 155));
                ctr.setPixel(x, y, 0xFF000000 | (g << 8));
            }
        }
    }

    /**
     * Finds the intersection(s), if any, of the line and the sphere that this {@link Planet} is
     * represented by.
     * @param from A point on the line.
     * @param to Another point on the line.
     * @return A set containing all intersections of the sphere and this line. This can be an empty
     * {@link Set} if there are no solutions, a set containing one {@link Vector3d} if the line
     * meets the sphere at a tangent, or two positions if it passes through the sphere.
     */
    private Set<Vector3d> findIntersections(final Vector3d from, final Vector3d to) {
        final double xf = from.getX();
        final double yf = from.getY();
        final double zf = from.getZ();

        final double xt = to.getX();
        final double yt = to.getY();
        final double zt = to.getZ();

        final double xs = this.sphereCentre.getX();
        final double ys = this.sphereCentre.getY();
        final double zs = this.sphereCentre.getZ();

        final double r = this.radius;

        // Calculate coefficients for lambda quadratic.
//        final double a = xd * xd + yd * yd + zd * zd;
//        final double b = 2 * ((xd * (xf - xs)) + (yd * (yf - ys)) + (zd * (zf - zs)));
//        final double c =
//            ((xs * xs) + (ys * ys) + (zs * zs)) - (2 * ((xs * xf) + (ys * yf) + (zs * zf))) - (r
//                * r);

        // a, b, c = xf, yf, zf
        // d, e, f = xt, yt, zt
        // i, j, k = xs, ys, zs

        //double power: +a2t2+b2t2+c2t2−2adt2+d2t2−2bet2+e2t2−2cft2+f2t2
        //single power: −2a2t−2b2t−2c2t+2adt+2bet+2cft+2ait−2dit+2bjt−2ejt+2ckt−2fkt
        //constant: a2+b2+c2−2ai+i2−2bj+j2−2ck+k2−r2
//        final double a = xf * xf + yf * yf + zf * zf
//                         - 2 * ((xf * xt) + (yf * yt) + (zf * zt))
//                         + xt * xt + yt * yt + zt * zt;
////                         + xs * xs + ys * ys + zs * zs - r * r;
//        final double b = - 2 * (xf * xf + yf * yf + zf * zf)
//                         + 2 * (xf * xt + yf * yt + zf * zt)
//                         - 2 * (xt * xs + yt * ys + zt * zs)
//                         + 2 * (xf * xs + yf * ys + zf * zs);
//        final double c = xt * xt + yt * yt + zt * zt
//                         - 2 * (xt * xs + yt * ys + zt * zs)
//                         + xs * xs + ys * ys + zs * zs
//                         - r * r;
//        final double c = xf * xf + yf * yf + zf * zf
//                        - 2 * (xf * xs + yf * ys + zf * zs)
//                        + xs * xs + ys * ys + zs * zs
//                        - r * r;

        // a^2 + b^2 + c^2
        // + i^2 + j^2 + k^2
        // - 2 * (a * i + b * j + c * k)
        final double a = xf * xf + yf * yf + zf * zf
            + xs * xs + ys * ys + zs * zs
            - 2 * (xf * xs + yf * ys + zf * zs);
        // - 2 * (a * a + b * b + c * c)
        // + 2 * (a * i + b * j + c * k)
        // - 2 * (d * i + e * j + f * k)
        // + 2 * (a * d + b * e + c * f)
        final double b = - 2 * (xf * xf + yf * yf + zf * zf)
            + 2 * (xf * xs + yf * ys + zf * zs)
            - 2 * (xt * xs + yt * ys + zt * zs)
            + 2 * (xf * xt + yf * yt + zf * zt);
        // d * d + e * e + f * f
        // + a * a + b * b + c * c
        // - 2 * (a * d + b * e + c * f)
        final double c = xt * xt + yt * yt + zt * zt
            + xt * xt + yt * yt + zt * zt
            - 2 * (xf * xt + yf * yt + zf * zt)
            - r * r;

        // Calculate determinant for the quadratic equation.
        final double determinant = b * b - (4 * a * c);
        if (determinant < 0) { // There are no (real) solutions.
            return Collections.emptySet();
        }

        // Calculate both possible values for parameter using the rest of the quadratic equation.
        final double t1 = (-b + Math.sqrt(determinant)) / (2 * a);
        final double t2 = (-b - Math.sqrt(determinant)) / (2 * a);

        final Set<Vector3d> intersections = new HashSet<>();
        // Using the parameter, find the intersection.
        intersections.add(from.add(to.sub(from).mul(t1)));
        if (t1 != t2) {
            // If the parameters are not equal (which would mean a tangential line), find the other
            // intersection.
            intersections.add(from.add(to.sub(from).mul(t2)));
        }

        return intersections;
    }

}
