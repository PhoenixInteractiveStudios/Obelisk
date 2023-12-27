[http-get]: https://img.shields.io/badge/GET-505CDC
[http-post]: https://img.shields.io/badge/POST-23A559
[http-put]: https://img.shields.io/badge/PUT-AC5A1F
[http-delete]: https://img.shields.io/badge/DELETE-A12828
[http-patch]: https://img.shields.io/badge/PATCH-AF7615

# Ticket resource

A support ticket, opened by a [user](User.md). Support tickets can be requests for help, bugreports, feedback or any
other issue that might require a private channel of communication between the team and one or more users.

## Ticket object

##### Ticket Structure

| Field | Type                                       | Description                                        |
|-------|--------------------------------------------|----------------------------------------------------|
| id    | turtle                                     | The ticket's unique id                             |
| title | ?string                                    | Title of this ticket (if set by user or moderator) |
| state | one of [ticket state](#Ticket-State-Types) | State of this ticket                               |
| tags  | array of strings                           | Additional tags added by user or moderator         |
| users | array of [user](User.md) ids               | Users that are involved in this ticket             |

##### Ticket State Types

| Name     | Description                                           |
|----------|-------------------------------------------------------|
| OPEN     | Ticket is not yet resolved and has not been abandoned |
| RESOLVED | Ticket and underlying issue are resolved              |
| FROZEN   | Ticket cannot be resolved at the time                 |
| CLOSED   | Ticket has been abandoned                             |
| DRAFT    | Ticket is not yet open                                |

## List ticket ids</br>![http-get] /tickets
Returns a list of all known ticket ids.

## Get ticket</br>![http-get] /tickets/[{ticket.id}](#ticket-object)
Returns a single [ticket object](#ticket-object).

## ![http-post] /tickets
Create a new ticket.
Returns the new [ticket object](#ticket-object) on success.

## Add ticket user</br>![http-put] /tickets/[{ticket.id}](#ticket-object)/users/[{user.id}](User.md#user-object)
Adds a user to a ticket.
Returns `204 No Content` on success.

## Remove ticket user</br>![http-delete] /tickets/[{ticket.id}](#ticket-object)/users/[{user.id}](User.md#user-object)
Removes a user from a ticket.
Returns `204 No Content` on success.

## Delete group</br>![http-delete] /tickets/[{ticket.id}](#ticket-object)
Permanently delete a ticket, identified by its id.
Returns `204 No Content` on success.

## ![http-patch] /tickets/[{ticket.id}](#ticket-object)
Modify a ticket, identified by its id.
Returns the modified [ticket object](#ticket-object) on success.