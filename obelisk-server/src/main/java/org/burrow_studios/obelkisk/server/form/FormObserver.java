package org.burrow_studios.obelkisk.server.form;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.burrow_studios.obelisk.api.entity.Form;
import org.burrow_studios.obelisk.api.form.FormElement;
import org.burrow_studios.obelisk.api.form.QueryElement;
import org.jetbrains.annotations.NotNull;

public class FormObserver extends ListenerAdapter {
    private final Form form;
    private final TextChannel channel;

    public FormObserver(@NotNull Form form, @NotNull TextChannel channel) {
        this.form = form;
        this.channel = channel;
    }

    private void next() {

    }

    private @NotNull FormElement nextElement() {
        for (FormElement element : this.form.getElements()) {
            if (element instanceof QueryElement<?> queryElement)
                if (queryElement.isDone()) continue;

            StringBuilder contentBuilder = new StringBuilder();
            if (element.getTitle() != null) {
                contentBuilder.append("# ");
                contentBuilder.append(element.getTitle());
                contentBuilder.append("\n");
            }
            if (element.getDescription() != null) {
                contentBuilder.append(element.getDescription());
            }
            String expectedContent = contentBuilder.toString();

            // check previous messages & try to match them to element
            for (Message message : this.channel.getIterableHistory()) {
                // TODO: fail condition

                // ignore if the message was not sent by the bot
                if (!message.getAuthor().equals(this.channel.getJDA().getSelfUser())) continue;

                String actualContent = message.getContentRaw();

                if (expectedContent.equals(actualContent)) {
                    // TODO
                } else {
                    // TODO ?
                }
            }
        }

        return null;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (!event.getChannel().equals(this.channel)) return;


    }
}
