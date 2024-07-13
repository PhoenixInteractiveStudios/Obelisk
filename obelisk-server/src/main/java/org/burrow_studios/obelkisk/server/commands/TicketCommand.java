package org.burrow_studios.obelkisk.server.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageCreateAction;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.burrow_studios.obelisk.api.entity.Ticket;
import org.burrow_studios.obelkisk.server.Obelisk;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class TicketCommand extends ListenerAdapter {
    private final Obelisk obelisk;

    public TicketCommand(@NotNull Obelisk obelisk) {
        this.obelisk = obelisk;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("ticket")) return;

        if ("list".equals(event.getSubcommandName())) {
            event.deferReply(true).queue();

            List<? extends Ticket> tickets = this.obelisk.getTicketDAO().listTickets();

            if (tickets.isEmpty()) {
                event.getHook().sendMessage("There are no open tickets").queue();
                return;
            }

            WebhookMessageCreateAction<Message> response = event.getHook().sendMessage("");

            Guild guild = event.getGuild();
            assert guild != null;

            for (Ticket ticket : tickets) {
                response.addContent("`" + ticket.getId() + "`");
                response.addContent("  ");

                TextChannel channel = guild.getTextChannelById(ticket.getChannelId());

                if (channel != null) {
                    response.addContent(channel.getAsMention());
                } else {
                    response.addContent("_Unknown channel_");
                }
            }

            response.queue();
            return;
        }

        if ("info".equals(event.getSubcommandName())) {
            event.deferReply(true).queue();

            @SuppressWarnings("DataFlowIssue") // required option
            int id = event.getOption("id").getAsInt();

            Optional<Ticket> optTicket = this.obelisk.getTicketDAO().getTicket(id);

            if (optTicket.isEmpty()) {
                event.getHook().sendMessage("Ticket does not exist").queue();
                return;
            }

            Ticket ticket = optTicket.get();

            WebhookMessageCreateAction<Message> response = event.getHook().sendMessage("#");
            response.addContent(" Ticket `" + ticket.getId() + "`");
            response.addContent("\n");
            response.addContent("**" + ticket.getUsers().size() + "** user(s)\n\n");

            response.queue();
            return;
        }
    }

    public @NotNull CommandData getData() {
        CommandDataImpl data = new CommandDataImpl("ticket", "Manage tickets");
        data.setDefaultPermissions(DefaultMemberPermissions.DISABLED);

        data.addSubcommands(new SubcommandData("list", "List all open tickets"));
        data.addSubcommands(new SubcommandData("info", "Display information on a single ticket")
                .addOption(OptionType.INTEGER, "id", "Ticket id", true)
        );

        return data;
    }
}
