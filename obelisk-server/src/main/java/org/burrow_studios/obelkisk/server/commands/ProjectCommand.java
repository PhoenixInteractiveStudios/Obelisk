package org.burrow_studios.obelkisk.server.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageCreateAction;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.burrow_studios.obelisk.api.entity.Project;
import org.burrow_studios.obelkisk.server.Obelisk;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ProjectCommand extends ListenerAdapter {
    private final Obelisk obelisk;

    public ProjectCommand(@NotNull Obelisk obelisk) {
        this.obelisk = obelisk;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("project")) return;

        if ("list".equals(event.getSubcommandName())) {
            event.deferReply(true).queue();

            List<? extends Project> projects = this.obelisk.getProjectDAO().listProjects();

            if (projects.isEmpty()) {
                event.getHook().sendMessage("There are no projects").queue();
                return;
            }

            WebhookMessageCreateAction<Message> response = event.getHook().sendMessage("");

            for (Project project : projects) {
                response.addContent("`" + project.getId() + "`");
                response.addContent("  ");
                response.addContent(project.getProjectTitle());
                response.addContent("   ");
                response.addContent("(" + project.getMembers().size() + " members)");
                response.addContent("\n");
            }

            response.queue();
            return;
        }

        if ("create".equals(event.getSubcommandName())) {
            event.deferReply(true).queue();

            @SuppressWarnings("DataFlowIssue") // required option
            String title = event.getOption("title").getAsString();

            Project project = this.obelisk.getProjectDAO().createProject(title);

            event.getHook().sendMessage("Created project \"" + project.getProjectTitle() + "\"").queue();
            return;
        }

        if ("info".equals(event.getSubcommandName())) {
            event.deferReply(true).queue();

            @SuppressWarnings("DataFlowIssue") // required option
            int id = event.getOption("id").getAsInt();

            Optional<Project> optProject = this.obelisk.getProjectDAO().getProject(id);

            if (optProject.isEmpty()) {
                event.getHook().sendMessage("Project does not exist").queue();
                return;
            }

            Project project = optProject.get();

            WebhookMessageCreateAction<Message> response = event.getHook().sendMessage("# ");
            response.addContent(project.getProjectTitle());
            response.addContent("\n");
            response.addContent("**" + project.getMembers().size() + "** member(s)\n\n");
            response.addContent("Invite-only: **" + project.isInviteOnly() + "**\n\n");
            response.addContent("Application template: `" + project.getApplicationTemplate() + "`");

            response.queue();
            return;
        }
    }

    public @NotNull CommandData getData() {
        CommandDataImpl data = new CommandDataImpl("project", "Perform CRUD operations on the project DB");
        data.setDefaultPermissions(DefaultMemberPermissions.DISABLED);

        data.addSubcommands(new SubcommandData("list", "List all existing projects"));
        data.addSubcommands(new SubcommandData("create", "Create a new project")
                .addOption(OptionType.STRING, "title", "Title of the project", true)
        );
        data.addSubcommands(new SubcommandData("info", "Display information on a single project")
                .addOption(OptionType.INTEGER, "id", "Project id", true)
        );

        return data;
    }
}
