package org.burrow_studios.obelkisk.server;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.burrow_studios.obelisk.api.entity.dao.*;
import org.burrow_studios.obelisk.util.ResourceTools;
import org.burrow_studios.obelkisk.server.commands.ProjectCommand;
import org.burrow_studios.obelkisk.server.commands.TicketCommand;
import org.burrow_studios.obelkisk.server.db.file.FSFormDB;
import org.burrow_studios.obelkisk.server.db.sql.DatabaseImpl;
import org.burrow_studios.obelkisk.server.event.EventManager;
import org.burrow_studios.obelkisk.server.form.FormManager;
import org.burrow_studios.obelkisk.server.listeners.DiscordAccountListener;
import org.burrow_studios.obelkisk.server.listeners.TicketCreateListener;
import org.burrow_studios.obelkisk.server.persistence.PersistentConfig;
import org.burrow_studios.obelkisk.server.text.TextProvider;
import org.burrow_studios.obelkisk.server.ticket.TicketManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Obelisk {
    private static final Logger LOG = LoggerFactory.getLogger(Obelisk.class);

    private EventManager eventManager;
    private TicketManager ticketManager;
    private FormManager formManager;
    private TextProvider textProvider;
    private DatabaseImpl database;
    private FSFormDB formDB;
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

        LOG.info("Initializing EventManager");
        this.eventManager = new EventManager();

        LOG.info("Parsing text.json");
        this.textProvider = new TextProvider(new File(Main.DIR, "text.json"));

        LOG.debug("Attempting to read persistent config");
        this.persistentConfig = new PersistentConfig(new File(Main.DIR, "persistence.json"));

        LOG.info("Initializing database");
        this.database = new DatabaseImpl(this, new File(Main.DIR, "obelisk.db"));
        this.formDB   = new     FSFormDB(this, new File(Main.DIR, "forms"));

        LOG.info("Initializing TicketManager");
        this.ticketManager = new TicketManager(this);

        LOG.info("Initializing FormManager");
        this.formManager = new FormManager(this);

        LOG.info("Initializing JDA");
        ProjectCommand projectCommand = new ProjectCommand(this);
        TicketCommand ticketCommand = new TicketCommand(this);
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
                // LISTENERS
                .addEventListeners(new DiscordAccountListener(this))
                .addEventListeners(new TicketCreateListener(this))
                // COMMANDS
                .addEventListeners(projectCommand)
                .addEventListeners(ticketCommand)
                .build();

        this.jda.awaitReady();

        this.jda.getPresence().setStatus(OnlineStatus.ONLINE);

        LOG.info("Validating config");
        Guild guild = this.config.validate(this.jda);

        LOG.info("Indexing form templates");
        this.formManager.onLoad(this.jda);

        LOG.info("Upserting commands");
        guild.upsertCommand(projectCommand.getData()).queue();
        guild.upsertCommand(ticketCommand.getData()).queue();

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
        this.formManager = null;
        this.ticketManager = null;
        this.textProvider = null;

        this.persistentConfig = null;
        this.config = null;

        LOG.info("OK bye");
    }

    public @NotNull Config getConfig() {
        return this.config;
    }

    public @NotNull PersistentConfig getPersistentConfig() {
        return this.persistentConfig;
    }

    public @NotNull EventManager getEventManager() {
        return this.eventManager;
    }

    public @NotNull TextProvider getTextProvider() {
        return this.textProvider;
    }

    public @NotNull TicketManager getTicketManager() {
        return this.ticketManager;
    }

    public @NotNull FormManager getFormManager() {
        return this.formManager;
    }

    public @NotNull UserDAO getUserDAO() {
        return this.database;
    }

    public @NotNull TicketDAO getTicketDAO() {
        return this.database;
    }

    public @NotNull ProjectDAO getProjectDAO() {
        return this.database;
    }

    public @NotNull DiscordAccountDAO getDiscordAccountDAO() {
        return this.database;
    }

    public @NotNull MinecraftAccountDAO getMinecraftDAO() {
        return this.database;
    }

    public @NotNull FormDAO getFormDAO() {
        return this.formDB;
    }
}