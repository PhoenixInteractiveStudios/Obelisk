package org.burrow_studios.obelisk.admin.commands;

import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(
        name = "list",
        aliases = "ls",
        mixinStandardHelpOptions = true
)
public class ApplicationListCommand implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        return 0;
    }
}
