package me.dylancurzon.nea.util;

public class Benchmark {

    private final String name;
    private final int callsPerOutput;
    private final boolean disabled;

    // reset every output
    private int calls = 0;
    private long cumulativeDuration = 0;
    private long startedAt = 0;

    public Benchmark(final String name, final int callsPerOutput, final boolean disabled) {
        this.name = name;
        this.callsPerOutput = callsPerOutput;
        this.disabled = disabled;
    }

    public void start() {
        if (this.disabled) return;
        this.startedAt = System.nanoTime();
    }

    public void end() {
        if (this.disabled) return;
        if (this.calls++ > this.callsPerOutput) {
            final double millis = ((double) (this.cumulativeDuration / this.callsPerOutput)) / 1000000.0;
            System.out.println("[BENCH] " + this.name + ": avg. " + millis + "ms (" + this.callsPerOutput + " calls)");
            this.calls = 0;
            this.cumulativeDuration = 0;
            this.startedAt = 0;
            return;
        }
        this.cumulativeDuration += (System.nanoTime() - this.startedAt);
        this.startedAt = System.nanoTime();
    }

}
