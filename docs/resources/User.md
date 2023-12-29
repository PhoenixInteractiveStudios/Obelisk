[http-get]: https://img.shields.io/badge/GET-505CDC
[http-post]: https://img.shields.io/badge/POST-23A559
[http-put]: https://img.shields.io/badge/PUT-AC5A1F
[http-delete]: https://img.shields.io/badge/DELETE-A12828
[http-patch]: https://img.shields.io/badge/PATCH-AF7615

# User resource

This resource represents an individual that is part of the team or community of BurrowStudios. Users can be part of
[Groups](Group.md), Tickets and Projects, and they have Permissions.

## User object

##### User Structure

| Field     | Type                | Description                            |
|-----------|---------------------|----------------------------------------|
| id        | turtle              | The user's unique id                   |
| name      | string              | The user's display name                |
| discord   | array of snowflakes | Discord accounts linked to this user   |
| minecraft | array of UUIDs      | Minecraft accounts linked to this user |

## List user ids</br>![http-get] /users
Returns a list of all known user ids.

## Get user</br>![http-get] /users/[{user.id}](#user-object)
Returns a single [user object](#user-object).

## ![http-post] /users
Create a new user.
Returns the new [user object](#user-object) on success.

## Delete user</br>![http-delete] /users/[{user.id}](#user-object)
Permanently delete a user, identified by its id.
Returns `204 No Content` on success.

## ![http-patch] /users/[{user.id}](#user-object)
Modify a user, identified by its id.
Returns the modified [user object](#user-object) on success.