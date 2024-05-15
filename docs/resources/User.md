[http-get]: https://img.shields.io/badge/GET-505CDC
[http-post]: https://img.shields.io/badge/POST-23A559
[http-put]: https://img.shields.io/badge/PUT-AC5A1F
[http-delete]: https://img.shields.io/badge/DELETE-A12828
[http-patch]: https://img.shields.io/badge/PATCH-AF7615

# User resource

This resource represents an individual that is part of the team or community.

## User Object

#### User Structure

| Field     | Type                                              | Description                            |
|-----------|---------------------------------------------------|----------------------------------------|
| id        | turtle                                            | The user's unique id                   |
| name      | string                                            | The user's display name                |
| discord   | array of [DiscordAccounts](DiscordAccount.md)     | Discord accounts linked to this user   |
| minecraft | array of [MinecraftAccounts](MinecraftAccount.md) | Minecraft accounts linked to this user |

An example of a user JSON:

```json
{
  "id": 1234567890,
  "name": "Larry",
  "discord": [
    {
      "snowflake": 987654321,
      "displayName": "LarryDiscord",
      "userName": "larrydiscord"
    }
  ],
  "minecraft": [
    {
      "uuid": "653d7671-beb5-45a0-b956-a06c91f3ce78",
      "name": "LarryMC"
    },
    {
      "uuid": "c2422236-c795-44d1-818c-4051d9cb413f",
      "name": "__Larryboi__"
    }
  ]
}
```

#### Reduced User Structure

A reduced user has only the id and name fields.


## List user ids <br> ![http-get] /users
Returns a list of all users as [reduced user objects](#reduced-user-structure).


## Get user <br> ![http-get] /users/[{user.id}](#user-object)
Returns a single [user object](#user-structure).


## Create user <br> ![http-post] /users
Create a new user.
Returns the new [user object](#user-object) on success.


## Delete user</br> ![http-delete] /users/[{user.id}](#user-object)
Permanently delete a user, identified by its id.
Returns `204 No Content` on success.


## Edit user <br> ![http-patch] /users/[{user.id}](#user-object)
Modify a user, identified by its id.
Returns the modified [user object](#user-object) on success.