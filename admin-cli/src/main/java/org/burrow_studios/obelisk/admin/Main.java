package org.burrow_studios.obelisk.admin;

import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new Command());
        commandLine.execute(args);
    }
}
