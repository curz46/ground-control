package me.dylancurzon.nea.gfx.gui;

public class QuarticEaseInAnimation extends Animation {

    /**
     * @see Animation for information on this class' parameters.
     */
    public QuarticEaseInAnimation(final double min, final double max, final int duration) {
        super(min, max, duration);
    }

    @Override
    public double determineValue() {
        final double x = ((double) this.ticks) / this.duration;
        return 1 - Math.pow(x - 1, 4);
    }

}
