package org.burrow_studios.obelisk.admin.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.admin.RequestHelper;
import org.burrow_studios.obelisk.core.http.Route;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Callable;

@Command(
        name = "create",
        mixinStandardHelpOptions = true
)
public class ApplicationCreateCommand implements Callable<Integer> {
    @SuppressWarnings("unused")
    @Option(names = {"-n", "--name"}, description = "Name of the application", required = true)
    private String name;

    @SuppressWarnings({"unused", "MismatchedReadAndWriteOfArray"})
    @Option(names = {"-i", "--intent"}, description = "An intent that should be granted to the application", arity = "0..*")
    private String[] intents;

    @Override
    public Integer call() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("name", name);

        JsonArray intentArr = new JsonArray();
        for (String intent : intents)
            intentArr.add(intent);
        if (!intentArr.isEmpty())
            requestBody.add("intents", intentArr);

        HttpRequest.Builder builder = RequestHelper.get(Route.Admin.CREATE_APPLICATION.compile(), requestBody);
        HttpRequest request = builder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        if (response.statusCode() == 201)
            return 0;
        return 1;
    }
}
