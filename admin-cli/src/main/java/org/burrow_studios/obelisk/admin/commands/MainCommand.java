package org.burrow_studios.obelisk.admin.commands;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command
public class MainCommand implements Callable<Integer> {
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
