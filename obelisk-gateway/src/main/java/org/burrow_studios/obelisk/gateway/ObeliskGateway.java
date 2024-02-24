package org.burrow_studios.obelisk.gateway;

import org.burrow_studios.obelisk.commons.util.ResourceTools;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.burrow_studios.obelisk.commons.yaml.YamlUtil;
import org.burrow_studios.obelisk.gateway.net.NetworkHandler;
import org.burrow_studios.obelisk.gateway.service.ServiceRegistry;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ObeliskGateway {
    private final @NotNull YamlSection config;
    private final @NotNull File configFile = new File(Main.DIR, "config.yaml");

    private final NetworkHandler networkHandler;
    private final ServiceRegistry serviceRegistry;

    ObeliskGateway() throws IOException, TimeoutException {
        ResourceTools resourceTools = ResourceTools.get(Main.class);
        YamlUtil.saveDefault(configFile, resourceTools.getResource("config.yaml"));

        this.config = YamlUtil.load(configFile, YamlSection.class);

        this.networkHandler  = new  NetworkHandler(this, config.getAsSection("net"));
        this.serviceRegistry = new ServiceRegistry(this, config.getAsSection("registry"));
    }

    void stop() throws IOException {
        this.config.save(configFile);
        this.networkHandler.close();
        this.serviceRegistry.close();
    }

    public @NotNull NetworkHandler getNetworkHandler() {
        return networkHandler;
    }
}
