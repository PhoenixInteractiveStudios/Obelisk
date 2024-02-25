package org.burrow_studios.obelisk.chramel;

import org.burrow_studios.obelisk.chramel.database.Database;
import org.burrow_studios.obelisk.chramel.database.SQLiteDB;
import org.burrow_studios.obelisk.commons.util.ResourceTools;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.burrow_studios.obelisk.commons.yaml.YamlUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Chramel {
    private static final Logger LOG = Logger.getLogger("MAIN");

    private final @NotNull YamlSection config;
    private final @NotNull File configFile = new File(Main.DIR, "config.yaml");

    private final Database database;

    Chramel() throws Exception {
        ResourceTools resourceTools = ResourceTools.get(Main.class);
        YamlUtil.saveDefault(configFile, resourceTools.getResource("config.yaml"));

        this.config = YamlUtil.load(configFile, YamlSection.class);

        LOG.log(Level.INFO, "Starting Database");
        this.database = new SQLiteDB(new File(Main.DIR, "chramel.db"));
    }

    void stop() throws Exception {
        LOG.log(Level.WARNING, "Shutting down");
        this.config.save(configFile);
        LOG.log(Level.INFO, "OK bye");
    }
}
