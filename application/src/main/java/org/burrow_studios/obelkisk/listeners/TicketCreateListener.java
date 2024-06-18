package org.burrow_studios.obelkisk.listeners;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.burrow_studios.obelkisk.Obelisk;
import org.jetbrains.annotations.NotNull;

public class TicketCreateListener extends ListenerAdapter {
    private final Obelisk obelisk;

    public TicketCreateListener(@NotNull Obelisk obelisk) {
        this.obelisk = obelisk;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        final long expectedChannel = this.obelisk.getConfig().ticketCreateChannel();
        final long   actualChannel = event.getChannelIdLong();

        if (expectedChannel != actualChannel) return;

        // TODO
    }
}
