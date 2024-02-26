package org.burrow_studios.obelisk.bruno;

import org.burrow_studios.obelisk.commons.util.ResourceTools;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.burrow_studios.obelisk.commons.yaml.YamlUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bruno {
    private static final Logger LOG = Logger.getLogger("MAIN");

    private final @NotNull YamlSection config;
    private final @NotNull File configFile = new File(Main.DIR, "config.yaml");

    Bruno() throws Exception {
        ResourceTools resourceTools = ResourceTools.get(Main.class);
        YamlUtil.saveDefault(configFile, resourceTools.getResource("config.yaml"));

        this.config = YamlUtil.load(configFile, YamlSection.class);
    }

    void stop() throws Exception {
        LOG.log(Level.WARNING, "Shutting down");
        this.config.save(configFile);
        LOG.log(Level.INFO, "OK bye");
    }
}
