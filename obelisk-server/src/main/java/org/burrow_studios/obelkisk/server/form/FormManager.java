package org.burrow_studios.obelkisk.server.form;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.burrow_studios.obelisk.api.FormTemplateMeta;
import org.burrow_studios.obelisk.api.entity.DiscordAccount;
import org.burrow_studios.obelisk.api.entity.Form;
import org.burrow_studios.obelisk.api.entity.User;
import org.burrow_studios.obelkisk.server.Obelisk;
import org.burrow_studios.obelkisk.server.event.EventHandler;
import org.burrow_studios.obelkisk.server.event.EventListener;
import org.burrow_studios.obelkisk.server.event.ExecutionStage;
import org.burrow_studios.obelkisk.server.event.events.FormOpenEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class FormManager implements EventListener {
    private static final Logger LOG = LoggerFactory.getLogger(FormManager.class);

    private final Obelisk obelisk;
    private final Set<FormAccess> entries;

    public FormManager(@NotNull Obelisk obelisk) {
        this.obelisk = obelisk;
        this.entries = ConcurrentHashMap.newKeySet();
        this.obelisk.getEventManager().registerListener(this);
    }

    public void onLoad(@NotNull JDA jda) {
        List<FormTemplateMeta> templatesMeta = this.obelisk.getFormDAO().listTemplates();

        for (FormTemplateMeta meta : templatesMeta) {
            TextChannel channel = jda.getTextChannelById(meta.accessChannel());

            if (channel == null) {
                LOG.warn("Skipping creation of form access \"{}\" because the access channel does not exist or is not reachable.", meta.identifier());
                continue;
            }

            this.createFormAccess(meta.identifier(), channel);
        }
    }

    private void createFormAccess(@NotNull String templateIdentifier, @NotNull TextChannel channel) {
        LOG.info("Creating form access for template \"{}\".", templateIdentifier);
        FormAccess formAccess = new FormAccess(this, channel, templateIdentifier);

        this.entries.add(formAccess);

        LOG.debug("Registering form access \"{}\" as JDA listener.", templateIdentifier);
        channel.getJDA().addEventListener(formAccess);
    }

    // interaction should already be filtered (this has to be an actual form open interaction)
    public void openForm(@NotNull String template, @NotNull IReplyCallback interaction) {
        // create user
        DiscordAccount discordAccount = this.obelisk.getDiscordAccountDAO().getDiscordAccount(interaction.getUser().getIdLong())
                .orElseGet(() -> this.obelisk.getDiscordAccountDAO().createDiscordAccount(interaction.getUser().getIdLong(), interaction.getUser().getName()));
        User u = discordAccount.getUser();
        if (u == null)
            u = this.obelisk.getUserDAO().createUser(discordAccount.getName(), null);
        final User user = u;

        this.obelisk.getEventManager().handle(new FormOpenEvent(interaction, discordAccount, template, user));
    }

    @EventHandler(stage = ExecutionStage.EXECUTE)
    public void openForm(@NotNull FormOpenEvent event) {
        // TODO: get actual category
        final long     categoryId = this.obelisk.getConfig().ticketCategory();
        final Category category   = event.getInteraction().getJDA().getCategoryById(categoryId);

        if (category == null) {
            String errorMsg = this.obelisk.getTextProvider().get("form.open.error");

            event.getInteraction().getHook()
                    .setEphemeral(true)
                    .sendMessage(errorMsg)
                    .queue();
            return;
        }

        event.getInteraction().deferReply(true).queue();

        TextChannel channel;
        try {
            // FIXME: placeholder name
            channel = category.createTextChannel("form").complete();
        } catch (RuntimeException e) {
            String errorMsg = this.obelisk.getTextProvider().get("form.open.error");
            event.getInteraction().getHook().sendMessage(errorMsg).queue();

            throw new RuntimeException("Failed to create new form channel", e);
        }
        event.setChannel(channel);

        Form form = this.obelisk.getFormDAO().createForm(event.getUser(), event.getTemplate());
        event.setForm(form);

        event.getInteraction().getHook().deleteOriginal().queue();
    }

    @EventHandler(stage = ExecutionStage.MONITOR)
    public void onOpen(@NotNull FormOpenEvent event) {
        // TODO: implement form dialogue

        String welcomeMsg = this.obelisk.getTextProvider().get("ticket.create.welcome", "user", event.getInteraction().getUser().getAsMention());

        event.getChannel().getManager().setName("form-" + event.getForm().getId()).queue();
        event.getChannel().sendMessage(welcomeMsg).queueAfter(1, TimeUnit.SECONDS);
    }
}
