package org.burrow_studios.obelkisk.server.form;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.SessionRecreateEvent;
import net.dv8tion.jda.api.events.session.SessionResumeEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class FormAccess extends ListenerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(FormAccess.class);

    static final String BUTTON_ID = "submit";

    private final FormManager manager;
    private final TextChannel channel;
    private final String template;
    private String content;

    private boolean active;

    public FormAccess(@NotNull FormManager manager, @NotNull TextChannel channel, @NotNull String template) {
        this.manager = manager;
        this.channel = channel;
        this.template = template;

        this.checkForUpdates();
    }

    public synchronized void checkForUpdates() {
        SelfUser selfUser = this.channel.getJDA().getSelfUser();
        MessageHistory history = this.channel.getHistoryFromBeginning(3).complete();

        if (history.size() > 2) {
            // don't log twice
            if (!active) return;

            LOG.warn("Switching to inactive, as there are more than 2 messages in the access channel");
            this.active = false;
            return;
        }

        // expected: original message & update
        if (history.size() == 2) {
            Message update   = history.getRetrievedHistory().get(0);
            Message original = history.getRetrievedHistory().get(1);

            // if the older message has not been sent by the bot, abort
            if (!original.getAuthor().equals(selfUser)) {
                // don't log twice
                if (!active) return;

                LOG.warn("Switching to inactive, as the first message in the access channel has not been sent by the self user");
                this.active = false;
                return;
            }

            // if both messages have been sent by the bot, delete the older one
            if (update.getAuthor().equals(selfUser)) {
                LOG.info("Deleting older message. A newer version has already been sent");
                this.active = true;

                original.delete().queue(unused -> {
                    // queue another update to check the message content
                    this.checkForUpdates();
                });
                return;
            }

            // if only the older message has been sent by the bot, use the newer one as a blueprint
            LOG.info("Updating access message. A new blueprint is available.");
            this.content = update.getContentRaw();
            original.delete().queue();
            update.delete().queue();
            this.sendMessage();
            return;
        }

        /*
         * 2 possible scenarios:
         * - Existing message sent by bot
         * - Initial blueprint message
         */
        if (history.size() == 1) {
            Message message = history.getRetrievedHistory().get(0);

            if (message.getAuthor().equals(selfUser)) {
                // existing message: check content
                String actualContent = message.getContentRaw();

                if (this.content.equals(actualContent)) {
                    LOG.debug("Existing message checks out.");
                    return;
                }

                LOG.info("Existing message does not match expected content. Re-sending.");
            } else {
                // initial blueprint

                LOG.info("Sending initial message based on existing blueprint.");
                this.content = message.getContentRaw();
            }

            // common part of both if-branches above
            message.delete().queue();
            this.sendMessage();
            return;
        }

        if (history.isEmpty()) {
            LOG.info("Access channel is empty, sending message.");
            this.sendMessage();
        }
    }

    private void sendMessage() {
        this.active = true;
        this.channel.sendMessage(this.content)
                .addActionRow(this.getButton())
                .queue();
    }

    private @NotNull Button getButton() {
        // TODO: customizable label & emoji
        return Button.of(ButtonStyle.SUCCESS, BUTTON_ID, "Submit", Emoji.fromFormatted("\uD83C\uDFAB"));
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (!event.getChannel().equals(this.channel)) return;

        final String id = event.getButton().getId();
        if (!Objects.equals(id, BUTTON_ID)) return;

        this.manager.submit(this.template, event);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        this.checkForUpdates();
    }

    @Override
    public void onSessionResume(@NotNull SessionResumeEvent event) {
        this.checkForUpdates();
    }

    @Override
    public void onSessionRecreate(@NotNull SessionRecreateEvent event) {
        this.checkForUpdates();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getChannel().equals(this.channel)) return;
        this.checkForUpdates();
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        if (!event.getChannel().equals(this.channel)) return;
        this.checkForUpdates();
    }

    @Override
    public void onMessageBulkDelete(@NotNull MessageBulkDeleteEvent event) {
        if (!event.getChannel().equals(this.channel)) return;
        this.checkForUpdates();
    }
}
