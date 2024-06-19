package org.burrow_studios.obelkisk.listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.burrow_studios.obelkisk.Obelisk;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TicketCreateListener extends ListenerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(TicketCreateListener.class);

    private static final String BUTTON_ID = "ticketCreate";

    private final Obelisk obelisk;
    private Long messageId;

    public TicketCreateListener(@NotNull Obelisk obelisk) {
        this.obelisk = obelisk;

        this.messageId = this.obelisk.getPersistentConfig().getLong("ticket.create.header.message").orElse(null);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        final long expectedChannel = this.obelisk.getConfig().ticketCreateChannel();
        final long   actualChannel = event.getChannelIdLong();

        if (expectedChannel != actualChannel) return;

        final long expectedMessage = this.messageId != null ? this.messageId : -1;
        final long   actualMessage = event.getMessageIdLong();

        if (expectedMessage != actualMessage) return;

        final String id = event.getButton().getId();
        if (!Objects.equals(id, BUTTON_ID)) return;

        final long     categoryId = this.obelisk.getConfig().ticketCategory();
        final Category category   = event.getJDA().getCategoryById(categoryId);

        if (category == null) {
            String errorMsg = this.obelisk.getTextProvider().get("ticket.create.error");

            event.getHook()
                    .setEphemeral(true)
                    .sendMessage(errorMsg)
                    .queue();
            return;
        }

        event.deferReply(true).queue();

        category.createTextChannel("ticket")
                .queue(channel -> {
                    this.obelisk.createTicket(channel.getIdLong(), "ticket");

                    event.getHook().deleteOriginal().queue();

                    String welcomeMsg = this.obelisk.getTextProvider().get("ticket.create.welcome", "user", event.getUser().getAsMention());

                    channel.sendMessage(welcomeMsg).queueAfter(1, TimeUnit.SECONDS);
                }, throwable -> {
                    String errorMsg = this.obelisk.getTextProvider().get("ticket.create.error");

                    event.getHook().sendMessage(errorMsg).queue();

                    LOG.warn("Failed to create channel for new ticket", throwable);
                });
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        final long msgId = this.messageId != null ? this.messageId : -1;

        if (event.getMessageIdLong() != msgId) return;

        // The header message has been deleted. Re-send it...
        this.sendMessage(event.getJDA());
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        if (this.messageId == null) {
            this.sendMessage(event.getJDA());
            return;
        }

        TextChannel channel = event.getJDA().getTextChannelById(this.obelisk.getConfig().ticketCreateChannel());

        if (channel == null) {
            LOG.warn("ticketCreateChannel does not exist or is inaccessible");
            this.messageId = null;
            return;
        }

        channel.retrieveMessageById(this.messageId).queue(message -> {
            // check message content
            final String expectedText = this.obelisk.getTextProvider().get("ticket.create.header");
            final String   actualText = message.getContentRaw();

            if (!Objects.equals(expectedText, actualText)) {
                // message content deviates; delete old message and send new
                message.delete().queue();

                this.sendMessage(event.getJDA());
                return;
            }

            // check button

            List<Button> buttons = message.getButtons();
            if (buttons.size() != 1) {
                // unexpected amount of buttons; delete old message and send new
                message.delete().queue();

                this.sendMessage(event.getJDA());
                return;
            }

            final Button expectedButton = this.getButton();
            final Button   actualButton = buttons.get(0);

            if (!Objects.equals(expectedButton, actualButton)) {
                // button deviates; delete old message and send new
                message.delete().queue();

                this.sendMessage(event.getJDA());
            }
        }, throwable -> {
            this.messageId = null;

            // if this error means the message does not exist, send it
            if (throwable instanceof ErrorResponseException e) {
                if (e.getErrorResponse().equals(ErrorResponse.UNKNOWN_MESSAGE)) {
                    this.sendMessage(event.getJDA());
                    return;
                }
            }

            // otherwise, this error is unintended
            LOG.warn("Could not retrieve ticketCreate header message", throwable);
        });
    }

    private void sendMessage(@NotNull JDA jda) {
        this.messageId = null;

        TextChannel channel = jda.getTextChannelById(this.obelisk.getConfig().ticketCreateChannel());

        if (channel == null) {
            LOG.warn("ticketCreateChannel does not exist or is inaccessible");
            return;
        }

        String messageContent = this.obelisk.getTextProvider().get("ticket.create.header");

        channel.sendMessage(messageContent)
                .addActionRow(getButton())
                .queue(message -> {
                    this.messageId = message.getIdLong();
                    this.obelisk.getPersistentConfig().setLong("ticket.create.header.message", this.messageId);
                }, throwable -> {
                    LOG.warn("Failed to send ticketCreate header", throwable);
                });
    }

    private @NotNull Button getButton() {
        String label = this.obelisk.getTextProvider().get("ticket.create.button");
        return Button.of(ButtonStyle.SUCCESS, BUTTON_ID, label, Emoji.fromFormatted("\uD83C\uDFAB"));
    }
}
