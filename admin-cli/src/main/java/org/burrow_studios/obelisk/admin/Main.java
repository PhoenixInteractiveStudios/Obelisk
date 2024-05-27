package org.burrow_studios.obelisk.admin;

import org.burrow_studios.obelisk.admin.commands.MainCommand;
import org.jetbrains.annotations.NotNull;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Config config = Config.get();

        CommandLine commandLine = new CommandLine(new MainCommand());
        commandLine.execute(args);
    }

    public static @NotNull File getConfigFile() throws URISyntaxException, IOException {
        URL jarLocation = Main.class.getProtectionDomain().getCodeSource().getLocation();

        File directory = new File(jarLocation.toURI()).getParentFile();
        File configFile = new File(directory, "config.properties");

        // copy default config
        if (!configFile.exists()) {
            InputStream defaultConfig = Main.class.getResourceAsStream("/config.properties");

            if (defaultConfig == null)
                throw new Error("Implementation error");

            Files.copy(defaultConfig, configFile.toPath());
        }

        return configFile;
    }
}
