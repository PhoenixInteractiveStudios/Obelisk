package org.burrow_studios.obelisk.moderationservice;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ModerationService {
    private static final Logger LOG = Logger.getLogger("MAIN");

    ModerationService() throws Exception {

    }

    void stop() throws Exception {
        LOG.log(Level.WARNING, "Shutting down");
        LOG.log(Level.INFO, "OK bye");
    }
}
