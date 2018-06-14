package me.dylancurzon.nea.util;

import java.awt.event.KeyEvent;

/**
 * A utility class for keeping track of which keys are pressed down whilst using Key Events.
 */
public class Keys {

    private static final boolean[] keys = new boolean[KeyEvent.RESERVED_ID_MAX];

    public static boolean pressed(final int keyCode) {
        return Keys.keys[keyCode];
    }

    public static void toggle(final int keyCode, final boolean value) {
        Keys.keys[keyCode] = value;
    }

}
