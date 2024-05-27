package org.burrow_studios.obelisk.admin;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@CommandLine.Command()
public class Command implements Callable<Integer> {
    @Option(names = {"-t", "--token"}, interactive = true)
    private String token;

    @Option(names = {"-v", "--verbose"}, defaultValue = "false")
    private boolean verbose;

    @Override
    public Integer call() throws Exception {
        if (verbose)
            setDebug();

        return 0;
    }
    
    private static void setDebug() {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.DEBUG);
    }
}
