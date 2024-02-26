package org.burrow_studios.obelisk.moderationservice;

import org.burrow_studios.obelisk.commons.util.ResourceTools;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.burrow_studios.obelisk.commons.yaml.YamlUtil;
import org.burrow_studios.obelisk.moderationservice.database.Database;
import org.burrow_studios.obelisk.moderationservice.database.ProjectDB;
import org.burrow_studios.obelisk.moderationservice.database.TicketDB;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModerationService {
    private static final Logger LOG = Logger.getLogger("MAIN");

    private final @NotNull YamlSection config;
    private final @NotNull File configFile = new File(Main.DIR, "config.yaml");

    private final Database database;

    ModerationService() throws Exception {
        ResourceTools resourceTools = ResourceTools.get(Main.class);
        YamlUtil.saveDefault(configFile, resourceTools.getResource("config.yaml"));

        this.config = YamlUtil.load(configFile, YamlSection.class);

        YamlSection dbConfig = this.config.getAsSection("db");

        LOG.log(Level.INFO, "Starting Database");
        this.database = new Database(
                dbConfig.getAsPrimitive("host").getAsString(),
                dbConfig.getAsPrimitive("port").getAsInt(),
                dbConfig.getAsPrimitive("user").getAsString(),
                dbConfig.getAsPrimitive("pass").getAsString(),
                dbConfig.getAsPrimitive("database").getAsString()
        );
    }

    void stop() throws Exception {
        LOG.log(Level.WARNING, "Shutting down");
        this.database.close();
        this.config.save(configFile);
        LOG.log(Level.INFO, "OK bye");
    }

    public @NotNull ProjectDB getProjectDB() {
        return database;
    }

    public @NotNull TicketDB getTicketDB() {
        return database;
    }
}
