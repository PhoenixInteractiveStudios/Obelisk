package org.burrow_studios.obelisk.gateway;

import org.burrow_studios.obelisk.commons.logging.LogTools;
import org.burrow_studios.obelisk.commons.util.ResourceTools;

import java.io.File;
import java.net.URISyntaxException;

public class Main {
    static {
        System.out.print("Starting Obelisk Gateway");
    }

    /** Static application version. */
    public static final String VERSION = ResourceTools.get(Main.class).getVersion();

    /** Directory in which the JAR ist located. */
    public static final File DIR;
    static {
        File f;
        try {
            f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        } catch (URISyntaxException e) {
            System.out.println("Failed to declare directory");
            throw new RuntimeException(e);
        }
        DIR = f;
    }

    /** JVM entrypoint */
    public static void main(String[] args) throws Exception {
        if (VERSION == null)
            throw new AssertionError("Unknown version");
        System.out.printf(" version %s...%n", VERSION);

        LogTools.get(new File(Main.DIR, "logs")).init();

        ObeliskGateway gateway = new ObeliskGateway();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                gateway.stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }
}