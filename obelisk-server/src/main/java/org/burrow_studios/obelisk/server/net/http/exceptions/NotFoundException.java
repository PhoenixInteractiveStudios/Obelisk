package org.burrow_studios.obelisk.server.net.http.exceptions;

import org.burrow_studios.obelisk.server.net.http.Response;
import org.burrow_studios.obelisk.server.net.http.ResponseBuilder;

public class NotFoundException extends APIException {
    @Override
    public Response asResponse() {
        return new ResponseBuilder()
                .setCode(404)
                .build();
    }
}
