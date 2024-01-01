[http-get]: https://img.shields.io/badge/GET-505CDC
[http-post]: https://img.shields.io/badge/POST-23A559
[http-put]: https://img.shields.io/badge/PUT-AC5A1F
[http-delete]: https://img.shields.io/badge/DELETE-A12828
[http-patch]: https://img.shields.io/badge/PATCH-AF7615

# Issue Board resource

## Board object

##### Board Structure

| Field  | Type                                       | Description                                        |
|--------|--------------------------------------------|----------------------------------------------------|
| id     | turtle                                     | The board's unique id                              |
| title  | string                                     | Title of this board                                |
| group  | turtle                                     | Id of the [group](Group.md) that manages this bard |
| tags   | array of [tag](#tag-structure) objects     | Available tags                                     |
| issues | array of [issue](#issue-structure) objects | Issues that are being tracked on this board        |

##### Tag structure

| Field | Type   | Description                                             |
|-------|--------|---------------------------------------------------------|
| id    | turtle | The tag's unique id                                     |
| board | turtle | Id of the [board](#board-structure) this tag belongs to |
| name  | string | Display name of this tag                                |

## Issue object

##### Issue Structure

| Field     | Type                                      | Description                                               |
|-----------|-------------------------------------------|-----------------------------------------------------------|
| id        | turtle                                    | The issue's unique id                                     |
| board     | turtle                                    | Id of the [board](#board-structure) this issue belongs to |
| author    | turtle                                    | Id of the [user](User.md) that opened this issue          |
| assignees | array of [user](User.md) ids              | Users that have been assigned to this issue               |
| title     | string                                    | Title of this issue                                       |
| state     | one of [ticket state](#issue-state-types) | State of this issue                                       |
| tags      | array of [tag](#tag-structure) objects    | Additional tags added by any participant                  |

##### Issue State Types

| Name      | Description                                          |
|-----------|------------------------------------------------------|
| OPEN      | Issue is not yet resolved and has not been abandoned |
| RESOLVED  | Issue is resolved                                    |
| FROZEN    | Issue cannot be resolved at this time                |
| ABANDONED | Issue has been abandoned                             |

## List issue board ids</br>![http-get] /boards
Returns a list of all known board ids.

## Get issue board</br>![http-get] /boards/[{board.id}](#board-object)
Returns a single [board object](#board-object).

## Create issue board</br>![http-post] /boards
Create a new board.
Returns the new [board object](#board-object) on success.

## Delete issue board</br>![http-delete] /boards/[{board.id}](#board-object)
Permanently delete a board, identified by its id.
Returns `204 No Content` on success.

## Modify issue board</br>![http-patch] /boards/[{board.id}](#board-object)
Modify a board, identified by its id.
Returns the modified [board object](#board-object) on success.

## List tag ids</br>![http-get] /boards/[{board.id}](#board-object)/tags
Returns a list of all known tag ids.

## Get tag</br>![http-get] /boards/[{board.id}](#board-object)/tags/[{tag.id}](#tag-structure)
Returns a single [tag object](#tag-structure).

## Create tag</br>![http-post] /boards/[{board.id}](#board-object)/tags
Create a new tag.
Returns the new [tag object](#tag-structure) on success.

## Delete tag</br>![http-delete] /boards/[{board.id}](#board-object)/tags/[{tag.id}](#tag-structure)
Permanently delete a tag, identified by its id.
Returns `204 No Content` on success.

## Modify tag</br>![http-patch] /boards/[{board.id}](#board-object)/tags/[{tag.id}](#tag-structure)
Modify a tag, identified by its id.
Returns the modified [tag object](#tag-structure) on success.

## List issue ids</br>![http-get] /boards/[{board.id}](#board-object)/issues
Returns a list of all known issue ids.

## Get issue</br>![http-get] /boards/[{board.id}](#board-object)/issues/[{issue.id}](#issue-object)
Returns a single [issue object](#issue-object).

## Create issue</br>![http-post] /boards/[{board.id}](#board-object)/issues
Create a new issue.
Returns the new [issue object](#issue-object) on success.

## Assign user to an issue</br>![http-put] /boards/[{board.id}](#board-object)/issues/[{issue.id}](#issue-object)/assignees/[{user.id}](User.md#user-object)
Adds an assignee to an issue.
Returns `204 No Content` on success.

## Remove an assigned user from an issue</br>![http-delete] /boards/[{board.id}](#board-object)/issues/[{issue.id}](#issue-object)/assignees/[{user.id}](User.md#user-object)
Removes an assignee from an issue.
Returns `204 No Content` on success.

## Add tag to issue</br>![http-put] /boards/[{board.id}](#board-object)/issues/[{issue.id}](#issue-object)/tags/[{tag.id}](#tag-structure)
Adds a tag to an issue.
Returns `204 No Content` on success.

## Remove tag from tag</br>![http-delete] /boards/[{board.id}](#board-object)/issues/[{issue.id}](#issue-object)/tags/[{tag.id}](#tag-structure)
Removes a tag from an issue.
Returns `204 No Content` on success.

## Delete issue</br>![http-delete] /boards/[{board.id}](#board-object)/issues/[{issue.id}](#issue-object)
Permanently delete an issue, identified by its id.
Returns `204 No Content` on success.

## Modify issue</br>![http-patch] /boards/[{board.id}](#board-object)/issues/[{issue.id}](#issue-object)
Modify an issue, identified by its id.
Returns the modified [issue object](#issue-object) on success.
