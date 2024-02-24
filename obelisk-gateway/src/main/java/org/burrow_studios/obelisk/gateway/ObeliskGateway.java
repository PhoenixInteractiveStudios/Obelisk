package org.burrow_studios.obelisk.gateway;

import org.burrow_studios.obelisk.commons.util.ResourceTools;
import org.burrow_studios.obelisk.commons.yaml.YamlUtil;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class ObeliskGateway {
    private final @NotNull YamlSection config;
    private final @NotNull File configFile = new File(Main.DIR, "config.yaml");

    ObeliskGateway() throws IOException {
        ResourceTools resourceTools = ResourceTools.get(Main.class);
        YamlUtil.saveDefault(configFile, resourceTools.getResource("config.yaml"));

        this.config = YamlUtil.load(configFile, YamlSection.class);
    }

    void stop() throws IOException {
        this.config.save(configFile);
    }
}
