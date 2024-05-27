package org.burrow_studios.obelisk.admin;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class Config {
    private static Config SINGLETON;

    private final String token;
    private final String url;

    private Config() throws IOException, URISyntaxException {
        File file = Main.getConfigFile();

        FileReader reader = new FileReader(file);
        Properties properties = new Properties();
        properties.load(reader);

        this.token = properties.getProperty("token");
        this.url   = properties.getProperty("url");
    }

    public static synchronized @NotNull Config get() throws IOException, URISyntaxException {
        if (SINGLETON == null)
            SINGLETON = new Config();
        return SINGLETON;
    }

    public @NotNull String getToken() {
        return this.token;
    }

    public @NotNull String getUrl() {
        return this.url;
    }
}
