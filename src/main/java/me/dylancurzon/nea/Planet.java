package me.dylancurzon.nea;

import com.flowpowered.noise.Noise;
import com.flowpowered.noise.NoiseQuality;
import me.dylancurzon.nea.gfx.PixelContainer;
import me.dylancurzon.nea.gfx.Renderable;
import me.dylancurzon.nea.util.PolarCoord;
import me.dylancurzon.nea.util.Vector3d;
import me.dylancurzon.nea.world.Tickable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Planet implements Tickable, Renderable {

    private static final int ROTATION_INCREMENTS = 500;

    private final Vector3d eyePosition = Vector3d.of(11.5, 0, 0);
    private final Vector3d viewPositionA = Vector3d.of(10.5, 0.5, 0.5);
    private final Vector3d viewPositionB = Vector3d.of(10.5, -0.5, -0.5);
    private final Vector3d sphereCentre = Vector3d.of(0, 0, 0);
    private final double radius = 5;

    private final int seed = ThreadLocalRandom.current().nextInt(100000);
    
    private final Vector3d[][] spherePositions;
    private final PolarCoord[][] polarCoordinates;
//    private final double[][][] rotatedNoiseValues;
    private final double[][] noiseMap; // theta, pi, noise
    private int ticks;
    private static int rotationX;
    private static int rotationY;

    public Planet(final int width, final int height) {
        System.out.println(PolarCoord.of(10, (float) (Math.PI - 0.1), 10).toVector3d());
        System.out.println(PolarCoord.of(10, (float) (Math.PI + 0.1), 10).toVector3d());

        this.spherePositions = this.calculateSpherePositions(width, height);
        this.polarCoordinates = new PolarCoord[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (this.spherePositions[x][y] == null) continue;
                this.polarCoordinates[x][y] = this.spherePositions[x][y].toSpherical();
//                System.out.println(this.polarCoordinates[x][y]);
            }
        }
        this.noiseMap = this.calculateNoiseValues();
    }

    public static void rotate(final int x, final int y) {
        rotationX = (rotationX + x) % ROTATION_INCREMENTS;
        rotationY = (rotationY + y) % ROTATION_INCREMENTS;

        if (rotationX < 0) {
            rotationX = ROTATION_INCREMENTS + rotationX;
        }
        if (rotationY < 0) {
            rotationY = ROTATION_INCREMENTS + rotationY;
        }
    }

    @Override
    public void render(final PixelContainer ctr) {
        final int centreX = ctr.getWidth() / 2;
        final int centreY = ctr.getHeight() / 2;
        for (int x = 0; x < ctr.getWidth(); x++) {
            for (int y = 0; y < ctr.getHeight(); y++) {
                final Vector3d position = this.spherePositions[x][y];
                if (position == null) {
                    ctr.setPixel(x, ctr.getHeight() - 1 - y, 0xFF);
                    continue;
                }
//                final PolarCoord coord = position.rotateY(this.rotation / 100.0).toSpherical();
                final PolarCoord coord = this.polarCoordinates[x][y];
                final int tinc = (int) (((coord.getTheta() / (Math.PI * 2)) * ROTATION_INCREMENTS) + rotationX) % ROTATION_INCREMENTS;
                final int pinc = (int) (((coord.getPhi() / (Math.PI * 2)) * ROTATION_INCREMENTS) + rotationY) % ROTATION_INCREMENTS;
                final double noise = this.noiseMap[tinc][pinc] + 1;
                final int n = (int) (noise * 255 / 2);
                final int argb = 0xFF000000 | (n << 16) | (n << 8) | n;
                ctr.setPixel(x, ctr.getHeight() - 1 - y, argb);
                if (x == centreX && y == centreY) {
//                    System.out.println("PHI: " + (coord.getTheta() + ((((double) rotationX) / ROTATION_INCREMENTS) * Math.PI * 2)));
//                    System.out.println("PHI: " + (coord.getPhi() + ((((float) rotationY) / ROTATION_INCREMENTS) * Math.PI * 2)));
//                    System.out.println(pinc);
                }
            }
        }
    }

    private double[][] calculateNoiseValues() {
        final double[][] values = new double[ROTATION_INCREMENTS][ROTATION_INCREMENTS];
//        for (int i = 0; i < increments; i++) {
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    final Vector3d position = spherePositions[x][y];
//                    if (position == null) continue;
//                    final double angle = ((Math.PI * 2) / increments) * i;
//                    final Vector3d rotated = position.rotateY(angle);
//                    values[i][x][y] = this.generateNoise(rotated);
//                }
//            }
//        }
        for (int tinc = 0; tinc < ROTATION_INCREMENTS; tinc++) {
            final float theta = (float) ((Math.PI * 2) / ROTATION_INCREMENTS) * tinc;
            for (int pinc = 0; pinc < ROTATION_INCREMENTS; pinc++) {
                final float phi = (float) ((Math.PI * 2) / ROTATION_INCREMENTS) * pinc;
                final PolarCoord coord = PolarCoord.of(5, theta, phi);
                if (pinc < 5 || pinc > 495) {
                    System.out.println("generate, pinc:" + pinc + ", PHI: " + phi + ", vec: " + coord.toVector3d());
                    System.out.println("tinc: " + tinc + ", THETA: " + theta);
                }
                values[tinc][pinc] = this.generateNoise(coord.toVector3d());
            }
        }
        return values;
    }

    private double generateNoise(final Vector3d pos) {
        return Noise.valueCoherentNoise3D(
            pos.getX(),
            pos.getY(),
            pos.getZ(),
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
     * intersections which are "behind" the <code>from</code> parameter.
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

    @Override
    public void tick() {

    }
}
