package org.burrow_studios.obelisk.gateway;

import org.burrow_studios.obelisk.commons.rpc.RPCServer;
import org.burrow_studios.obelisk.commons.rpc.http.SunServerImpl;
import org.burrow_studios.obelisk.commons.util.ResourceTools;
import org.burrow_studios.obelisk.commons.yaml.YamlUtil;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.burrow_studios.obelisk.gateway.service.ServiceRegistry;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ObeliskGateway {
    private final @NotNull YamlSection config;
    private final @NotNull File configFile = new File(Main.DIR, "config.yaml");
    private final ServiceRegistry serviceRegistry;
    private final RPCServer<?> server;

    ObeliskGateway() throws IOException, TimeoutException {
        ResourceTools resourceTools = ResourceTools.get(Main.class);
        YamlUtil.saveDefault(configFile, resourceTools.getResource("config.yaml"));

        this.config = YamlUtil.load(configFile, YamlSection.class);

        this.serviceRegistry = new ServiceRegistry(config.getAsSection("registry"));

        this.server = new SunServerImpl(config.getAsPrimitive("port").getAsInt());
    }

    void stop() throws IOException {
        this.config.save(configFile);
    }
}
