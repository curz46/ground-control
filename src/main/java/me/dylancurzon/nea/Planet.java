package me.dylancurzon.nea;

import com.flowpowered.noise.Noise;
import com.flowpowered.noise.NoiseQuality;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.Renderable;
import me.dylancurzon.nea.util.Vector3d;
import me.dylancurzon.nea.world.Tickable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Planet implements Tickable, Renderable {

    private final Vector3d eyePosition = Vector3d.of(111, 0, 0);
    private final Vector3d viewPositionA = Vector3d.of(100, 0.5, 0.5);
    private final Vector3d viewPositionB = Vector3d.of(100, -0.5, -0.5);
    private final Vector3d sphereCentre = Vector3d.of(0, 0, 0);
    private final double radius = 5;

    private final int seed = ThreadLocalRandom.current().nextInt(100000);
    
    private final Vector3d[][] spherePositions;
    private int ticks;

    public Planet(final int width, final int height) {
        this.spherePositions = this.calculateSpherePositions(width, height);
    }
    
    @Override
    public void tick() {
        this.ticks++;
    }

    @Override
    public void render(final PixelContainer ctr) {
        for (int x = 0; x < ctr.getWidth(); x++) {
            for (int y = 0; y < ctr.getHeight(); y++) {
                final Vector3d position = this.spherePositions[x][y];
                if (position == null) {
                    ctr.setPixel(x, ctr.getHeight() - 1 - y, 0xFF);
                    continue;
                }
                final Vector3d rotated = position.rotateY((this.ticks / 100.0) % Math.PI * 2);
                final double noise = this.generateNoise(rotated);
                ctr.setPixel(x, ctr.getHeight() - 1 - y, (int) (0xFFFF * noise));
            }
        }
    }

    private double generateNoise(final Vector3d vector) {
        return Noise.valueCoherentNoise3D(
            vector.getX(),
            vector.getY(),
            vector.getZ(),
            this.seed,
            NoiseQuality.BEST
        );
    }

    private Vector3d[][] calculateSpherePositions(final int width, final int height) {
        final Vector3d[][] positions = new Vector3d[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Using linear interpolation, find position of the pixel centre.
                final Vector3d pixelCentre = this.viewPositionA.add(
                    this.viewPositionB.sub(this.viewPositionA).mul(
                        Vector3d.of(
                            0,
                            ((double) y + 0.5) / height,
                            ((double) x + 0.5) / width
                        )
                    )
                );
                final Optional<Vector3d> closestIntersection = this.findClosestIntersection(this.eyePosition, pixelCentre);
                if (!closestIntersection.isPresent()) continue;
                final Vector3d vector = closestIntersection.get();
                positions[x][y] = vector;
//                final Vector3d rotated = vector.rotateY(this.ticks / 100.0);
//                final double noise = Noise.valueCoherentNoise3D(
//                    rotated.getX(),
//                    rotated.getY(),
//                    rotated.getZ(),
//                    this.seed,
//                    NoiseQuality.FAST
//                );
            }
        }
        return positions;
    }

    private Optional<Vector3d> findClosestIntersection(final Vector3d from, final Vector3d to) {
        final Set<Vector3d> intersections = this.findIntersections(from, to);
        return intersections.stream().min((a, b) -> a.sub(from).abs() > b.sub(from).abs() ? 1 : -1);
    }

    /**
     * Finds the intersection(s), if any, of the line and the sphere that this {@link Planet} is
     * represented by.
     * @param from A point on the line.
     * @param to Another point on the line.
     * @return A set containing all intersections of the sphere and this line. This will be an empty
     * {@link Set} if there are no solutions, a set containing one {@link Vector3d} if the line
     * meets the sphere at a tangent, or two positions if it passes through the sphere. This method will not return
     * intersections which are "behind" the <code>to</code> parameter.
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

        // Using http://www.ambrsoft.com/TrigoCalc/Sphere/SpherLineIntersection_.htm as reference.

        // Calculate coefficients for lambda quadratic.
        final double a = Math.pow(xt - xf, 2) + Math.pow(yt - yf, 2) + Math.pow(zt - zf, 2);
        final double b = 2 * ((xt - xf) * (xf - xs) + (yt - yf) * (yf - ys) + (zt - zf) * (zf - zs));
        final double c = xs * xs + ys * ys + zs * zs
                       + xf * xf + yf * yf + zf * zf
                       - 2 * (xf * xs + yf * ys + zf * zs)
                       - r * r;

        // Calculate discriminant for the quadratic equation.
        final double discriminant = b * b - (4 * a * c);
        if (discriminant < 0) { // There are no (real) solutions.
            return Collections.emptySet();
        }

        // Calculate both possible values for parameter using the rest of the quadratic equation.
        final double t1 = (-b + Math.sqrt(discriminant)) / (2 * a);
        final double t2 = (-b - Math.sqrt(discriminant)) / (2 * a);

        final Set<Vector3d> intersections = new HashSet<>();
        // Using the parameter, find the intersection.
        if (t1 >= 0) intersections.add(from.add(to.sub(from).mul(t1)));
        if (t1 != t2 && t2 >= 0) {
            // If the parameters are not equal (which would mean a tangential line), find the other
            // intersection.
            intersections.add(from.add(to.sub(from).mul(t2)));
        }

        return intersections;
    }

}
