package org.burrow_studios.obelisk.admin.commands;

import picocli.CommandLine.Command;

@Command(
        name = "application",
        aliases = "app",
        mixinStandardHelpOptions = true,
        subcommands = ApplicationListCommand.class
)
public class ApplicationCommand {

}
