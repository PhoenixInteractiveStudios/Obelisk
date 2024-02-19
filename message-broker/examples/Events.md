# Internal Events

These are the internal events that are used in backend communication.

Entity events are brokered over the `entity_events_exchange` topic exchange.

Internal events are not guaranteed to represent [Gateway events](/docs/Events.md) 1:1.

## Group Events

Generic catchall: `group.#`

### GroupCreate
Fires when a new group is created.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.group.GroupCreateEvent`
- Routing key: `group.create`

#### Example
```json
{
  "id": 123456789,
  "name": "GroupCreate",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "name": "Tunnelbois",
    "position": 3
  }
}
```


### GroupDelete
Fires when a group is deleted.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.group.GroupDeleteEvent`
- Routing key: `group.delete`

#### Example
```json
{
  "id": 123456789,
  "name": "GroupDelete",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321
  }
}
```


### GroupUpdateName
Fires when the name of a group is modified.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.group.GroupUpdateNameEvent`
- Routing key: `group.update.name`

#### Example
```json
{
  "id": 123456789,
  "name": "GroupUpdateName",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "oldValue": "Tunnelbois",
    "newValue": "Frens"
  }
}
```


### GroupUpdatePosition
Fires when the position of a group is modified.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.group.GroupUpdatePositionEvent`
- Routing key: `group.update.position`

#### Example
```json
{
  "id": 123456789,
  "name": "GroupUpdatePosition",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "oldValue": 3,
    "newValue": 2
  }
}
```


### GroupAddMember
Fires when a user is added to a group.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.group.GroupUpdateMembersEvent`
- Routing key: `group.update.members.add`

#### Example
```json
{
  "id": 123456789,
  "name": "GroupAddMember",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "val": 789456123
  }
}
```


### GroupRemoveMember
Fires when a user is removed from a group.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.group.GroupUpdateMembersEvent`
- Routing key: `group.update.members.remove`

#### Example
```json
{
  "id": 123456789,
  "name": "GroupRemoveMember",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "val": 789456123
  }
}
```


## Project Events

Generic catchall: `project.#`

### ProjectCreate
Fires when a new project is created.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.project.ProjectCreateEvent`
- Routing key: `project.create`

#### Example

```json
{
  "id": 123456789,
  "name": "ProjectCreate",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "title": "Minecraft VIRO",
    "timings": {
      "release": "2019-09-29T16:00:00Z",
      "apply": "2019-09-29T16:00:00Z",
      "start": "2019-10-23T19:00:00Z",
      "end": "2019-11-29T21:00:00Z"
    },
    "state": "ENDED",
    "members": [
      456789123,
      123789456,
      123123456
    ]
  }
}
```


### ProjectDelete
Fires when a project is deleted.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.project.ProjectDeleteEvent`
- Routing key: `project.delete`

#### Example
```json
{
  "id": 123456789,
  "name": "ProjectDelete",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321
  }
}
```


### ProjectUpdateState
Fires when the state of a project is modified.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.project.ProjectUpdateStateEvent`
- Routing key: `project.update.state`

#### Example
```json
{
  "id": 123456789,
  "name": "ProjectUpdateState",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "oldValue": "PLANNING",
    "newValue": "CANCELED"
  }
}
```


### ProjectUpdateTitle
Fires when the title of a project is modified.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.project.ProjectUpdateTitleEvent`
- Routing key: `project.update.title`

#### Example
```json
{
  "id": 123456789,
  "name": "ProjectUpdateTitle",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "oldValue": "VIRO",
    "newValue": "Minecraft VIRO"
  }
}
```


### ProjectUpdateTiming
Fires when a timing of a project is modified.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.project.ProjectUpdateTitleEvent`
- Routing key: `project.update.title`

#### Example
```json
{
  "id": 123456789,
  "name": "ProjectUpdateTitle",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "name": "release",
    "oldValue": "null",
    "newValue": "2019-09-29T16:00:00Z"
  }
}
```


### ProjectAddMember
Fires when a user is added to a project.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.project.ProjectUpdateMembersEvent`
- Routing key: `project.update.discord.add`

#### Example
```json
{
  "id": 123456789,
  "name": "ProjectAddMember",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "val": 789456123
  }
}
```


### ProjectRemoveMember
Fires when a user is removed from a project.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.project.ProjectUpdateMembersEvent`
- Routing key: `project.update.members.remove`

#### Example
```json
{
  "id": 123456789,
  "name": "ProjectRemoveMember",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "val": 789456123
  }
}
```


## Ticket Events

Generic catchall: `ticket.#`

### TicketCreate
Fires when a new ticket is created.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.ticket.TicketCreateEvent`
- Routing key: `ticket.create`

#### Example

```json
{
  "id": 123456789,
  "name": "TicketCreate",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "title": "Need help in-game",
    "state": "OPEN",
    "tags": [
      "support",
      "minecraft"
    ],
    "users": [
      456789123
    ]
  }
}
```


### TicketDelete
Fires when a ticket is deleted.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.ticket.TicketDeleteEvent`
- Routing key: `ticket.delete`

#### Example
```json
{
  "id": 123456789,
  "name": "TicketDelete",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321
  }
}
```


### TicketUpdateState
Fires when the state of a ticket is modified.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.ticket.TicketUpdateStateEvent`
- Routing key: `ticket.update.state`

#### Example
```json
{
  "id": 123456789,
  "name": "TicketUpdateState",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "oldValue": "OPEN",
    "newValue": "RESOLVED"
  }
}
```


### TicketUpdateTitle
Fires when the title of a ticket is modified.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.ticket.TicketUpdateTitleEvent`
- Routing key: `ticket.update.title`

#### Example
```json
{
  "id": 123456789,
  "name": "TicketUpdateTitle",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "oldValue": "Need hjaelp ingame",
    "newValue": "Need help in-game"
  }
}
```


### TicketAddTag
Fires when a tag is added to a ticket.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.ticket.TicketUpdateTagsEvent`
- Routing key: `ticket.update.tags.add`

#### Example
```json
{
  "id": 123456789,
  "name": "TicketAddTag",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "val": "minecraft"
  }
}
```


### TicketRemoveTag
Fires when a tag is removed from a ticket.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.ticket.TicketUpdateTagsEvent`
- Routing key: `ticket.update.tags.remove`

#### Example
```json
{
  "id": 123456789,
  "name": "TicketRemoveTag",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "val": "minecraft"
  }
}
```


### TicketAddUser
Fires when a user is added to a ticket.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.ticket.TicketUpdateUsersEvent`
- Routing key: `ticket.update.users.add`

#### Example
```json
{
  "id": 123456789,
  "name": "TicketAddUser",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "val": 789456123
  }
}
```


### TicketRemoveUser
Fires when a user is removed from a ticket.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.ticket.TicketUpdateUsersEvent`
- Routing key: `ticket.update.users.remove`

#### Example
```json
{
  "id": 123456789,
  "name": "TicketRemoveUser",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "val": 789456123
  }
}
```


## User Events

Generic catchall: `user.#`

### UserCreate
Fires when a new user is created.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.user.UserCreateEvent`
- Routing key: `user.create`

#### Example
```json
{
  "id": 123456789,
  "name": "UserCreate",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "name": "Larry",
    "discord": [
      1183185715887095838
    ],
    "minecraft": []
  }
}
```


### UserDelete
Fires when a user is deleted.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.user.UserDeleteEvent`
- Routing key: `user.delete`

#### Example
```json
{
  "id": 123456789,
  "name": "UserDelete",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321
  }
}
```


### UserUpdateName
Fires when the name of a user is modified.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.user.UserUpdateNameEvent`
- Routing key: `user.update.name`

#### Example
```json
{
  "id": 123456789,
  "name": "UserUpdateName",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "oldValue": "Larry",
    "newValue": "Bob"
  }
}
```


### UserAddDiscordId
Fires when a Discord user snowflake is assigned to a user.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.user.UserUpdateDiscordIdsEvent`
- Routing key: `user.update.discord.add`

#### Example
```json
{
  "id": 123456789,
  "name": "UserAddDiscordId",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "val": 1183185715887095838
  }
}
```


### UserRemoveDiscordId
Fires when a Discord user snowflake is removed from a user.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.user.UserUpdateDiscordIdsEvent`

- Exchange: `entity_events_exchange`
- Routing key: `user.update.discord.remove`

#### Example
```json
{
  "id": 123456789,
  "name": "UserRemoveDiscordId",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "val": 1183185715887095838
  }
}
```


### UserAddMinecraftId
Fires when a Minecraft account UUID is assigned to a user.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.user.UserUpdateMinecraftIdsEvent`
- Routing key: `user.update.minecraft.add`

#### Example
```json
{
  "id": 123456789,
  "name": "UserAddMinecraftId",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "val": "c919f678-8690-41d8-979a-fbd91c740bb1"
  }
}
```


### UserRemoveMinecraftId
Fires when a Minecraft account UUID is removed from a user.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.user.UserUpdateMinecraftIdsEvent`
- Routing key: `user.update.minecraft.remove`

#### Example
```json
{
  "id": 123456789,
  "name": "UserRemoveMinecraftId",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "val": "c919f678-8690-41d8-979a-fbd91c740bb1"
  }
}
```


## Issue Board Events

Generic catchall: `its.#`

- Board catchall: `its.board.#`
- Issue catchall: `its.issue.#`

### BoardCreate
Fires when a new issue board is created.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.board.board.BoardCreateEvent`
- Routing key: `its.board.create`

#### Example

```json
{
  "id": 123456789,
  "name": "BoardCreate",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "title": "Board title",
    "group": 456789123
  }
}
```


### BoardDelete
Fires when an issue board is deleted.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.board.board.BoardDeleteEvent`
- Routing key: `its.board.delete`

#### Example
```json
{
  "id": 123456789,
  "name": "BoardDelete",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321
  }
}
```


### BoardUpdateTitle
Fires when the title of an issue board is modified.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.board.board.BoardUpdateTitleEvent`
- Routing key: `its.board.update.title`

#### Example
```json
{
  "id": 123456789,
  "name": "BoardUpdateTitle",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "oldValue": "Old Board Title",
    "newValue": "New Board Title"
  }
}
```


### IssueCreate
Fires when a new issue is created.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.board.issue.IssueCreateEvent`
- Routing key: `its.issue.create`

#### Example

```json
{
  "id": 123456789,
  "name": "IssueCreate",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "board": 456789456,
    "title": "Issue title",
    "group": 456789123
  }
}
```


### IssueDelete
Fires when an issue is deleted.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.board.issue.IssueDeleteEvent`
- Routing key: `its.issue.delete`

#### Example
```json
{
  "id": 123456789,
  "name": "IssueDelete",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321
  }
}
```


### IssueUpdateTitle
Fires when the title of an issue is modified.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.board.issue.IssueUpdateTitleEvent`
- Routing key: `its.issue.update.title`

#### Example
```json
{
  "id": 123456789,
  "name": "IssueUpdateTitle",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "oldValue": "Old issue title",
    "newValue": "New issue title"
  }
}
```


### IssueUpdateState
Fires when the state of an issue is modified.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.board.issue.IssueUpdateStateEvent`
- Routing key: `its.issue.update.state`

#### Example
```json
{
  "id": 123456789,
  "name": "IssueUpdateState",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "oldValue": "OPEN",
    "newValue": "RESOLVED"
  }
}
```


### IssueAddAssignee
Fires when a user is assigned to an issue.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.board.issue.IssueUpdateAssigneesEvent`
- Routing key: `its.issue.update.assignees.add`

#### Example
```json
{
  "id": 123456789,
  "name": "IssueAddAssignee",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "val": 789456123
  }
}
```


### IssueRemoveAssignee
Fires when a user is removed from an issue.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.board.issue.IssueUpdateAssigneesEvent`
- Routing key: `its.issue.update.assignees.remove`

#### Example
```json
{
  "id": 123456789,
  "name": "IssueRemoveAssignee",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "val": 789456123
  }
}
```


### IssueAddTag
Fires when a tag is added to an issue.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.board.issue.IssueUpdateTagsEvent`
- Routing key: `its.issue.update.tags.add`

#### Example
```json
{
  "id": 123456789,
  "name": "IssueAddAssignee",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "val": 789456123
  }
}
```


### IssueRemoveTag
Fires when a tag is removed from an issue.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.board.issue.IssueUpdateTagsEvent`
- Routing key: `its.issue.update.tags.remove`

#### Example
```json
{
  "id": 123456789,
  "name": "IssueRemoveAssignee",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "val": 789456123
  }
}
```


### TagCreate
Fires when a new tag is created.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.board.tag.TagCreateEvent`
- Routing key: `its.tag.create`

#### Example

```json
{
  "id": 123456789,
  "name": "IssueCreate",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "board": 456789456,
    "title": "Issue title",
    "group": 456789123
  }
}
```


### TagDelete
Fires when a tag is deleted.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.board.tag.TagDeleteEvent`
- Routing key: `its.tag.delete`

#### Example
```json
{
  "id": 123456789,
  "name": "IssueDelete",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321
  }
}
```


### TagUpdateName
Fires when the name of a tag is modified.

- API equivalent: `org.burrow_studios.obelisk.api.event.entity.board.tag.TagUpdateNameEvent`
- Routing key: `its.tag.update.name`

#### Example
```json
{
  "id": 123456789,
  "name": "IssueUpdateTitle",
  "time": "2024-02-19T19:11:43Z",
  "data": {
    "id": 987654321,
    "oldValue": "old_tag_name",
    "newValue": "new_tag_name"
  }
}
```