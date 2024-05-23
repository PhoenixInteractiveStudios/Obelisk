package org.burrow_studios.obelisk.monolith;

import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;

import java.io.File;
import java.net.URISyntaxException;

public class Main {
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
    public static void main(String[] args) throws DatabaseException {
        ObeliskMonolith obelisk = new ObeliskMonolith();
    }
}
