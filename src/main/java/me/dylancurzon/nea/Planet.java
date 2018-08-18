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

    private static int width;
    private static int height;

    private final Vector3d eyePosition = Vector3d.of(11.5, 0, 0);
    private final Vector3d viewPositionA = Vector3d.of(10.5, 0.5, 0.5);
    private final Vector3d viewPositionB = Vector3d.of(10.5, -0.5, -0.5);
    private final Vector3d sphereCentre = Vector3d.of(0, 0, 0);
    private final double radius = 5;

    private final int seed = ThreadLocalRandom.current().nextInt(100000);
    
    private static Vector3d[][] baseSpherePositions;
    private static PolarCoord[][] basePolarCoordinates;
    private static Vector3d[][] rotatedSpherePositions;
    private static PolarCoord[][] rotatedPolarCoordinates;
//    private final double[][][] rotatedNoiseValues;
    private final double[][] noiseMap; // theta, pi, noise
    private final PolarCoord[][] actualPolar = new PolarCoord[ROTATION_INCREMENTS][ROTATION_INCREMENTS];
    private int ticks;
    private static int rotationX;
    private static int rotationY;

    public Planet(final int width, final int height) {
        this.width = width;
        this.height = height;
//        System.out.println( // PolarCoord{5.000000,3.759064,3.141593}
////            Vector3d.of(1.4148, Math.PI, Math.PI)
////                .toSpherical()
//            PolarCoord.of(5, (float) 0.5, (float) Math.PI)
//                .toVector3d()
//        );
//        System.out.println(Vector3d.of(5, 5, 5).toSpherical());

        System.out.println(Vector3d.of(1, 0, 0).toSpherical());
        System.out.println(Vector3d.of(1, 0, 0).toSpherical().toVector3d());
        System.out.println(Vector3d.of(0, 1, 0).toSpherical());
        System.out.println(Vector3d.of(0, 1, 0).toSpherical().toVector3d());
        System.out.println(Vector3d.of(0, 0, 1).toSpherical());
        System.out.println(Vector3d.of(0, 0, 1).toSpherical().toVector3d());

        System.out.println(Vector3d.of(-1, 0, 0).toSpherical());
        System.out.println(Vector3d.of(-1, 0, 0).toSpherical().toVector3d());
        System.out.println(Vector3d.of(0, -1, 0).toSpherical());
        System.out.println(Vector3d.of(0, -1, 0).toSpherical().toVector3d());
        System.out.println(Vector3d.of(0, 0, -1).toSpherical());
        System.out.println(Vector3d.of(0, 0, -1).toSpherical().toVector3d());

//        System.out.println(PolarCoord.of(5, (float) Math.PI - 0.1f, (float) Math.PI - 0.5f).toVector3d());

        baseSpherePositions = this.calculateSpherePositions(width, height);
        rotatedSpherePositions = new Vector3d[width][height];
        basePolarCoordinates = new PolarCoord[width][height];
        rotatedPolarCoordinates = new PolarCoord[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (baseSpherePositions[x][y] == null) continue;
                rotatedSpherePositions[x][y] = baseSpherePositions[x][y];
                final PolarCoord polar = baseSpherePositions[x][y].toSpherical();
                basePolarCoordinates[x][y] = polar;
                rotatedPolarCoordinates[x][y] = polar;
//                System.out.println(this.basePolarCoordinates[x][y]);
            }
        }
        this.noiseMap = this.calculateNoiseValues();
    }

    public static void rotate(final int x, final int y) {
        rotationX = (rotationX + x) % ROTATION_INCREMENTS; // 0->2PI
        rotationY = (rotationY + y) % ROTATION_INCREMENTS; // half because only 0->PI

        if (rotationX < 0) {
            rotationX = ROTATION_INCREMENTS + rotationX;
        }
        if (rotationY < 0) {
            rotationY = ROTATION_INCREMENTS + rotationY;
        }

        // rotationX should affect phi
        // rotationY should affect theta

        // first determine angle given min=0 max=ROTATION_INCREMENTS
        final double angleX = (((double) rotationX) / ROTATION_INCREMENTS) * (Math.PI * 2);
        final double angleY = (((double) rotationY) /ROTATION_INCREMENTS) * (Math.PI * 2);

//        Game.frame.setTitle(rotationX + ", " + rotationY + ", " + angleX + ", " + angleY);

        for (int xd = 0; xd < width; xd++) {
            for (int yd = 0; yd < height; yd++) {
                final Vector3d basePosition = baseSpherePositions[xd][yd];
                if (basePosition == null) continue;
                final Vector3d rotatedPosition = basePosition.rotateY(angleX).rotateZ(angleY);
                rotatedSpherePositions[xd][yd] = rotatedPosition;
                rotatedPolarCoordinates[xd][yd] = rotatedPosition.toSpherical();
            }
        }
    }

    @Override
    public void render(final PixelContainer ctr) {
        final int centreX = ctr.getWidth() / 2;
        final int centreY = ctr.getHeight() / 2;
        for (int x = 0; x < ctr.getWidth(); x++) {
            for (int y = 0; y < ctr.getHeight(); y++) {
                final Vector3d position = baseSpherePositions[x][y];
                if (position == null) {
                    ctr.setPixel(x, ctr.getHeight() - 1 - y, 0xFF);
                    continue;
                }
//                final PolarCoord coord = position.rotateY(this.rotation / 100.0).toSpherical();
                final PolarCoord coord = rotatedPolarCoordinates[x][y];
                final int tinc = (int) (coord.getTheta() / (Math.PI * 2) * ROTATION_INCREMENTS);
                final int pinc = (int) (coord.getUnsignedPhi() / Math.PI * (ROTATION_INCREMENTS / 2));
                final double noise = this.noiseMap[tinc][pinc] + 1;
                final int n = (int) (noise * 255 / 2);
                final int argb = 0xFF000000 | (n << 16) | (n << 8) | n;
                ctr.setPixel(x, ctr.getHeight() - 1 - y, argb);
                if (x == centreX && y == centreY) {
                    final PolarCoord actualPolarValue = this.actualPolar[tinc][pinc];
                    Game.frame.setTitle(actualPolarValue + ", " + actualPolarValue.toVector3d());
                }
            }
        }
    }

    private double[][] calculateNoiseValues() {
        final double[][] values = new double[ROTATION_INCREMENTS][ROTATION_INCREMENTS];
//        for (int i = 0; i < increments; i++) {
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    final Vector3d position = baseSpherePositions[x][y];
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
                final float phi = (float) ((Math.PI * 2) / ROTATION_INCREMENTS) * pinc - (float) Math.PI;
                final PolarCoord coord = PolarCoord.of(5, theta, phi);
                final Vector3d vector = coord.toVector3d();
                final double k = 2;
//                values[tinc][pinc] = (Math.floor(vector.getX() * k) + Math.floor(vector.getY() * k) + Math.floor(vector.getZ() * k)) % 2 == 0 ? -1 : (tinc);
                values[tinc][pinc] = generateNoise(vector);
                actualPolar[tinc][pinc] = coord;
            }
        }
        return values;
    }

    private double generateNoise(final Vector3d pos) {
        return Noise.valueCoherentNoise3D(
            pos.getX() / 1.2,
            pos.getY() / 1.2,
            pos.getZ() / 1.2,
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
