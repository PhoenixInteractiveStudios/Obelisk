package org.burrow_studios.obelisk.admin;

import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@CommandLine.Command()
public class Command implements Callable<Integer> {
    @Option(names = {"-t", "--token"}, interactive = true)
    private String token;

    @Option(names = {"-v", "--verbose"}, defaultValue = "false")
    private boolean verbose;

    @Override
    public Integer call() throws Exception {
        return 0;
    }
}
