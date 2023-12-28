[http-get]: https://img.shields.io/badge/GET-505CDC
[http-post]: https://img.shields.io/badge/POST-23A559
[http-put]: https://img.shields.io/badge/PUT-AC5A1F
[http-delete]: https://img.shields.io/badge/DELETE-A12828
[http-patch]: https://img.shields.io/badge/PATCH-AF7615

# Issue Board resource

## Issue Board object

##### Issue Board Structure

| Field  | Type                                       | Description                                        |
|--------|--------------------------------------------|----------------------------------------------------|
| id     | turtle                                     | The board's unique id                              |
| title  | string                                     | Title of this board                                |
| group  | turtle                                     | Id of the [group](Group.md) that manages this bard |
| tags   | array of [tag](#tag-structure) objects     | Available tags                                     |
| issues | array of [issue](#issue-structure) objects | Issues that are being tracked on this board        |

##### Tag structure

| Field | Type   | Description                                                         |
|-------|--------|---------------------------------------------------------------------|
| id    | turtle | The tag's unique id                                                 |
| board | turtle | Id of the [issue board](#issue-board-structure) this tag belongs to |
| name  | string | Display name of this tag                                            |

## Issue object

##### Issue Structure

| Field     | Type                                      | Description                                                           |
|-----------|-------------------------------------------|-----------------------------------------------------------------------|
| id        | turtle                                    | The issue's unique id                                                 |
| board     | turtle                                    | Id of the [issue board](#issue-board-structure) this issue belongs to |
| author    | turtle                                    | Id of the [user](User.md) that opened this issue                      |
| assignees | array of [user](User.md) ids              | Users that have been assigned to this issue                           |
| title     | string                                    | Title of this issue                                                   |
| state     | one of [ticket state](#issue-state-types) | State of this issue                                                   |
| tags      | array of [tag](#tag-structure) objects    | Additional tags added by any participant                              |

##### Issue State Types

| Name      | Description                                          |
|-----------|------------------------------------------------------|
| OPEN      | Issue is not yet resolved and has not been abandoned |
| RESOLVED  | Issue is resolved                                    |
| FROZEN    | Issue cannot be resolved at this time                |
| ABANDONED | Issue has been abandoned                             |

## List issue board ids</br>![http-get] /issue-boards
Returns a list of all known issue board ids.

## Get issue board</br>![http-get] /issue-boards/[{issue-board.id}](#issue-board-object)
Returns a single [issue board object](#issue-board-object).

## Create issue board</br>![http-post] /issue-boards
Create a new issue board.
Returns the new [issue board object](#issue-board-object) on success.

## Delete issue board</br>![http-delete] /issue-boards/[{issue-board.id}](#issue-board-object)
Permanently delete an issue board, identified by its id.
Returns `204 No Content` on success.

## Modify issue board</br>![http-patch] /issue-boards/[{issue-board.id}](#issue-board-object)
Modify an issue board, identified by its id.
Returns the modified [issue board object](#issue-board-object) on success.

## List tag ids</br>![http-get] /issue-boards/[{issue-board.id}](#issue-board-object)/tags
Returns a list of all known tag ids.

## Get tag</br>![http-get] /issue-boards/[{issue-board.id}](#issue-board-object)/tags/[{tag.id}](#tag-structure)
Returns a single [tag object](#tag-structure).

## Create tag</br>![http-post] /issue-boards/[{issue-board.id}](#issue-board-object)/tags
Create a new tag.
Returns the new [tag object](#tag-structure) on success.

## Delete tag</br>![http-delete] /issue-boards/[{issue-board.id}](#issue-board-object)/tags/[{tag.id}](#tag-structure)
Permanently delete a tag, identified by its id.
Returns `204 No Content` on success.

## Modify tag</br>![http-patch] /issue-boards/[{issue-board.id}](#issue-board-object)/tags/[{tag.id}](#tag-structure)
Modify a tag, identified by its id.
Returns the modified [tag object](#tag-structure) on success.

## List issue ids</br>![http-get] /issue-boards/[{issue-board.id}](#issue-board-object)/issues
Returns a list of all known issue ids.

## Get issue</br>![http-get] /issue-boards/[{issue-board.id}](#issue-board-object)/issues/[{issue.id}](#issue-object)
Returns a single [issue object](#issue-object).

## Create issue</br>![http-post] /issue-boards/[{issue-board.id}](#issue-board-object)/issues
Create a new issue.
Returns the new [issue object](#issue-object) on success.

## Delete issue</br>![http-delete] /issue-boards/[{issue-board.id}](#issue-board-object)/issues/[{issue.id}](#issue-object)
Permanently delete an issue, identified by its id.
Returns `204 No Content` on success.

## Modify issue</br>![http-patch] /issue-boards/[{issue-board.id}](#issue-board-object)/issues/[{issue.id}](#issue-object)
Modify an issue, identified by its id.
Returns the modified [issue object](#issue-object) on success.
