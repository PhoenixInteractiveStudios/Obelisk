package org.burrow_studios.obelisk.admin.commands;

import org.burrow_studios.obelisk.admin.RequestHelper;
import org.burrow_studios.obelisk.core.http.Route;
import picocli.CommandLine.Command;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Callable;

@Command(
        name = "list",
        aliases = "ls",
        mixinStandardHelpOptions = true
)
public class ApplicationListCommand implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest.Builder builder = RequestHelper.get(Route.Auth.LIST_APPLICATIONS.compile());
        HttpRequest request = builder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        return 0;
    }
}
