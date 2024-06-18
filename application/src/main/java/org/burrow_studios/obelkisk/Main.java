package org.burrow_studios.obelkisk;

import org.burrow_studios.obelisk.util.ResourceTools;

public class Main {
    static {
        System.out.print("Starting Obelisk");
    }

    public static final String VERSION = ResourceTools.get(Main.class).getVersion();

    public static void main(String[] args) {
        if (VERSION == null)
            throw new Error("Unknown version");
        System.out.printf(" version %s...%n", VERSION);
    }
}
