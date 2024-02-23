package org.burrow_studios.obelisk.commons.rpc.amqp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class AMQPUtils {
    static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private AMQPUtils() { }
}
