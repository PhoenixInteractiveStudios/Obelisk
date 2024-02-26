package org.burrow_studios.obelisk.userservice;

import org.burrow_studios.obelisk.commons.util.ResourceTools;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.burrow_studios.obelisk.commons.yaml.YamlUtil;
import org.burrow_studios.obelisk.userservice.database.EntityDatabase;
import org.burrow_studios.obelisk.userservice.database.GroupDB;
import org.burrow_studios.obelisk.userservice.database.UserDB;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {
    private static final Logger LOG = Logger.getLogger("MAIN");

    private final @NotNull YamlSection config;
    private final @NotNull File configFile = new File(Main.DIR, "config.yaml");

    private final EntityDatabase database;

    UserService() throws IOException {
        ResourceTools resourceTools = ResourceTools.get(Main.class);
        YamlUtil.saveDefault(configFile, resourceTools.getResource("config.yaml"));

        this.config = YamlUtil.load(configFile, YamlSection.class);

        YamlSection dbConfig = this.config.getAsSection("db");

        this.database = new EntityDatabase(
                dbConfig.getAsPrimitive("host").getAsString(),
                dbConfig.getAsPrimitive("port").getAsInt(),
                dbConfig.getAsPrimitive("database").getAsString(),
                dbConfig.getAsPrimitive("user").getAsString(),
                dbConfig.getAsPrimitive("pass").getAsString()
        );
    }

    void stop() throws Exception {
        LOG.log(Level.WARNING, "Shutting down");
        this.database.close();
        this.config.save(configFile);
        LOG.log(Level.INFO, "OK bye");
    }

    public @NotNull GroupDB getGroupDB() {
        return this.database;
    }

    public @NotNull UserDB getUserDB() {
        return this.database;
    }
}
