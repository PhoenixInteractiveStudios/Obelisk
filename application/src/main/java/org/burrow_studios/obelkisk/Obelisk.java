package org.burrow_studios.obelkisk;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.burrow_studios.obelisk.util.ResourceTools;
import org.burrow_studios.obelkisk.db.DatabaseImpl;
import org.burrow_studios.obelkisk.entity.DiscordAccount;
import org.burrow_studios.obelkisk.entity.Ticket;
import org.burrow_studios.obelkisk.listeners.TicketCreateListener;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Obelisk {
    private static final Logger LOG = LoggerFactory.getLogger(Obelisk.class);

    private DatabaseImpl database;
    private Config config;
    private JDA jda;

    public Obelisk() { }

    public void start() throws IOException, InterruptedException {
        LOG.info("Starting Obelisk...");

        LOG.debug("Creating default config file");
        ResourceTools.get(Main.class).createDefault(Main.DIR, "config.properties");

        LOG.debug("Reading config");
        this.config = Config.fromFile(new File(Main.DIR, "config.properties"));


        LOG.info("Initializing database");
        this.database = new DatabaseImpl(new File(Main.DIR, "obelisk.db"));

        LOG.info("Initializing JDA");
        this.jda = JDABuilder.create(config.token(),
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_MESSAGE_REACTIONS,
                    GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                    GatewayIntent.GUILD_MEMBERS)
                .disableCache(CacheFlag.ACTIVITY)
                .disableCache(CacheFlag.VOICE_STATE)
                .disableCache(CacheFlag.CLIENT_STATUS)
                .disableCache(CacheFlag.ONLINE_STATUS)
                .disableCache(CacheFlag.SCHEDULED_EVENTS)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .addEventListeners(new TicketCreateListener(this))
                .build();

        this.jda.awaitReady();

        this.jda.getPresence().setStatus(OnlineStatus.ONLINE);

        LOG.info("All done.");
    }

    public void stop() {
        LOG.info("Stopping...");

        if (this.jda != null) {
            try {
                this.jda.shutdown();
                this.jda.awaitShutdown();
            } catch (InterruptedException e) {
                LOG.warn("Could not properly shut down JDA", e);
            }
            this.jda = null;
        }

        if (this.database != null) {
            try {
                this.database.close();
            } catch (IOException e) {
                LOG.warn("Could not properly close database", e);
            }
            this.database = null;
        }

        LOG.info("OK bye");
    }

    public @NotNull Config getConfig() {
        return this.config;
    }

    public @NotNull Ticket createTicket(@NotNull String name) {
        return this.database.createTicket(name);
    }

    public @NotNull DiscordAccount createDiscordAccount(long snowflake, @NotNull String name) {
        return this.database.createDiscordAccount(snowflake, name);
    }
}
