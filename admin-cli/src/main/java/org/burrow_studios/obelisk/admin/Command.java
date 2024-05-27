package org.burrow_studios.obelisk.admin;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Callable;

@CommandLine.Command()
public class Command implements Callable<Integer> {
    @Option(names = {"-t", "--token"}, interactive = true)
    private String token;

    @Option(names = {"-v", "--verbose"}, defaultValue = "false")
    private boolean verbose;

    @Option(names = {"-c", "--config"})
    private File config;

    @Option(names = "--url")
    private String url;

    @Override
    public Integer call() throws Exception {
        if (verbose)
            setDebug();

        if (config == null)
            this.config = Main.getConfigFile();

        readConfig();

        return 0;
    }
    
    private static void setDebug() {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.DEBUG);
    }

    private void readConfig() throws IOException {
        FileReader reader = new FileReader(config);
        Properties properties = new Properties();
        properties.load(reader);

        String token = properties.getProperty("token");
        if (this.token == null)
            this.token = token;

        String url = properties.getProperty("url");
        if (this.url == null)
            this.url = url;
    }
}
