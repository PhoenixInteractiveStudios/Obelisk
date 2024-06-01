package org.burrow_studios.obelisk.admin.commands;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name = "obelisk-admin",
        mixinStandardHelpOptions = true,
        subcommands = ApplicationCommand.class
)
public class MainCommand {
    @Option(names = {"-v", "--verbose"}, defaultValue = "false")
    private boolean verbose;

    private static void setDebug() {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.DEBUG);
    }
}
