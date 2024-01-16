[http-get]: https://img.shields.io/badge/GET-505CDC

# Gateway Events

In addition to the HTTP/REST API, Obelisk offers a socket-based system for real-time communication. This includes events
(e.g. newly created, deleted or updated entities) and chat-like features.

## Get socket address</br>![http-get] /socket
Returns the address that should be used to establish a websocket connection.

#### Response structure

| Field | Type   | Description             |
|-------|--------|-------------------------|
| host  | string | The host server address |
| port  | int32  | The server port         |

An example of the response would be:

```json
{
  "host": "api.burrow_studios.org",
  "port": 8346
}
```