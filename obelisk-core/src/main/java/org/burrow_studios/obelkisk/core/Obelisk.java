package org.burrow_studios.obelkisk.core;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.burrow_studios.obelisk.util.ResourceTools;
import org.burrow_studios.obelkisk.core.db.file.FSFormDB;
import org.burrow_studios.obelkisk.core.db.interfaces.DiscordAccountDB;
import org.burrow_studios.obelkisk.core.db.interfaces.FormDB;
import org.burrow_studios.obelkisk.core.db.interfaces.TicketDB;
import org.burrow_studios.obelkisk.core.db.interfaces.UserDB;
import org.burrow_studios.obelkisk.core.db.sql.DatabaseImpl;
import org.burrow_studios.obelkisk.core.listeners.TicketCreateListener;
import org.burrow_studios.obelkisk.core.persistence.PersistentConfig;
import org.burrow_studios.obelkisk.core.text.TextProvider;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Obelisk {
    private static final Logger LOG = LoggerFactory.getLogger(Obelisk.class);

    private TextProvider textProvider;
    private DatabaseImpl database;
    private FormDB formDB;
    private JDA jda;

    private PersistentConfig persistentConfig;
    private Config config;

    public Obelisk() { }

    public void start() throws IOException, InterruptedException {
        LOG.info("Starting Obelisk...");

        LOG.debug("Creating default config file");
        ResourceTools.get(Main.class).createDefault(Main.DIR, "config.properties");

        LOG.debug("Creating default text.json");
        ResourceTools.get(Main.class).createDefault(Main.DIR, "text.json");

        LOG.debug("Reading config");
        this.config = Config.fromFile(new File(Main.DIR, "config.properties"));

        LOG.info("Parsing text.json");
        this.textProvider = new TextProvider(new File(Main.DIR, "text.json"));

        LOG.debug("Attempting to read persistent config");
        this.persistentConfig = new PersistentConfig(new File(Main.DIR, "persistence.json"));

        LOG.info("Initializing database");
        this.database = new DatabaseImpl(this, new File(Main.DIR, "obelisk.db"));
        this.formDB   = new     FSFormDB(this, new File(Main.DIR, "forms"));

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

        this.formDB = null;

        LOG.info("OK bye");
    }

    public @NotNull Config getConfig() {
        return this.config;
    }

    public @NotNull PersistentConfig getPersistentConfig() {
        return this.persistentConfig;
    }

    public @NotNull TextProvider getTextProvider() {
        return this.textProvider;
    }

    public @NotNull UserDB getUserDB() {
        return this.database;
    }

    public @NotNull TicketDB getTicketDB() {
        return this.database;
    }

    public @NotNull DiscordAccountDB getDiscordAccountDB() {
        return this.database;
    }

    public @NotNull FormDB getFormDB() {
        return this.formDB;
    }
}
