package org.burrow_studios.obelkisk.server.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageCreateAction;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.burrow_studios.obelisk.api.entity.Ticket;
import org.burrow_studios.obelkisk.server.Obelisk;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

            Map<Ticket, TextChannel> openTickets = new LinkedHashMap<>();
            List<? extends Ticket>    allTickets = this.obelisk.getTicketDAO().listTickets();

            for (Ticket ticket : allTickets) {
                // ignore if ticket has no channel
                TextChannel channel = event.getJDA().getTextChannelById(ticket.getChannelId());
                if (channel == null) continue;

                // ignore if ticket is archived
                if (channel.getParentCategoryIdLong() == this.obelisk.getConfig().ticketArchive()) continue;

                openTickets.put(ticket, channel);
            }

            if (openTickets.isEmpty()) {
                event.getHook().sendMessage("There are no open tickets").queue();
                return;
            }

            WebhookMessageCreateAction<Message> response = event.getHook().sendMessage("");

            Guild guild = event.getGuild();
            assert guild != null;

            openTickets.forEach((ticket, channel) -> {
                response.addContent("`" + ticket.getId() + "`");
                response.addContent("  " + channel.getAsMention());
            });

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

        if ("close".equals(event.getSubcommandName())) {
            event.deferReply(true).queue();

            Channel channel = event.getChannel();

            OptionMapping channelOption = event.getOption("channel");
            if (channelOption != null)
                channel = channelOption.getAsChannel();

            for (Ticket ticket : this.obelisk.getTicketDAO().listTickets()) {
                if (ticket.getChannelId() != channel.getIdLong()) continue;

                this.obelisk.getTicketManager().closeTicket(event.getJDA(), ticket);
                event.getHook().sendMessage("Ticket " + ticket.getId() + " has been closed.").queue();
                return;
            }

            event.getHook().sendMessage(channel.getAsMention() + " is not an open ticket channel!").queue();
        }
    }

    public @NotNull CommandData getData() {
        CommandDataImpl data = new CommandDataImpl("ticket", "Manage tickets");
        data.setDefaultPermissions(DefaultMemberPermissions.DISABLED);

        data.addSubcommands(new SubcommandData("list", "List all open tickets"));
        data.addSubcommands(new SubcommandData("info", "Display information on a single ticket")
                .addOption(OptionType.INTEGER, "id", "Ticket id", true)
        );
        data.addSubcommands(new SubcommandData("close", "Close an open ticket")
                .addOption(OptionType.CHANNEL, "channel", "Ticket channel", false)
        );

        return data;
    }
}
