[http-get]: https://img.shields.io/badge/GET-505CDC
[http-post]: https://img.shields.io/badge/POST-23A559
[http-put]: https://img.shields.io/badge/PUT-AC5A1F
[http-delete]: https://img.shields.io/badge/DELETE-A12828
[http-patch]: https://img.shields.io/badge/PATCH-AF7615

# Group resource

A group is a collection of [Users](User.md) that hava shared attributes. When a user is a member of a group it inherits
the groups attributes for as long as it is part of that group.

## Group object

##### Group Structure

| Field    | Type                         | Description                           |
|----------|------------------------------|---------------------------------------|
| id       | turtle                       | The group's unique id                 |
| name     | string                       | The group's display name              |
| size     | integer                      | Amount of members in this group       |
| members  | array of [user](User.md) ids | Users that are a member of this group |
| position | integer                      | Position of this group in group lists |

## List group ids</br>![http-get] /groups
Returns a list of all known group ids.

## Get group</br>![http-get] /groups/[{group.id}](#group-object)
Returns a single [group object](#group-object).

## ![http-post] /groups
Create a new group.
Returns the new [group object](#group-object) on success.

## Add group member</br>![http-put] /groups/[{group.id}](#group-object)/members/[{user.id}](User.md#user-object)
Adds a user to a group.
Returns `204 No Content` on success.

## Remove group member</br>![http-delete] /groups/[{group.id}](#group-object)/members/[{user.id}](User.md#user-object)
Removes a user from a group.
Returns `204 No Content` on success.

## Delete group</br>![http-delete] /groups/[{group.id}](#group-object)
Permanently delete a group, identified by its id.
Returns `204 No Content` on success.

## ![http-patch] /groups/[{group.id}](#group-object)
Modify a group, identified by its id.
Returns the modified [group object](#group-object) on success.