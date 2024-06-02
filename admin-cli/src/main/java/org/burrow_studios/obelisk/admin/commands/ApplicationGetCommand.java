package org.burrow_studios.obelisk.admin.commands;

import org.burrow_studios.obelisk.admin.RequestHelper;
import org.burrow_studios.obelisk.core.http.Route;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Callable;

@Command(
        name = "get",
        mixinStandardHelpOptions = true
)
public class ApplicationGetCommand implements Callable<Integer> {
    @Option(names = {"--id"}, required = true, description = "The application id")
    private long applicationId;

    @Override
    public Integer call() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest.Builder builder = RequestHelper.get(Route.Admin.GET_APPLICATION.compile(applicationId));
        HttpRequest request = builder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        return 0;
    }
}
