[http-get]: https://img.shields.io/badge/GET-505CDC
[http-post]: https://img.shields.io/badge/POST-23A559
[http-put]: https://img.shields.io/badge/PUT-AC5A1F
[http-delete]: https://img.shields.io/badge/DELETE-A12828
[http-patch]: https://img.shields.io/badge/PATCH-AF7615

# Project resource

## Project object

##### Project Structure

| Field   | Type                                                 | Description                             |
|---------|------------------------------------------------------|-----------------------------------------|
| id      | turtle                                               | The project's unique id                 |
| title   | string                                               | The project's title                     |
| timings | [project timings](#Project-Timings-Structure) object | Timing information                      |
| state   | one of [project state](#Project-State-Types)         | State of this project                   |
| members | array of [user](User.md) ids                         | Users that are a member of this project |

##### Project Timings Structure

| Field   | Type       | Description                                                           |
|---------|------------|-----------------------------------------------------------------------|
| release | ?timestamp | The exact time the project will be / has been released (announced)    |
| apply   | ?timestamp | The exact time the project will be / has been opened for applications |
| start   | ?timestamp | The exact time the project will be / has been started                 |
| end     | ?timestamp | The exact time the project will be / has been ended or stopped        |

##### Project State Types

| Name        | Description                                                               |
|-------------|---------------------------------------------------------------------------|
| CONCEPT     | There has not been any planning yet, the project is merely an idea        |
| PLANNING    | The project is actively being planned                                     |
| APPLICATION | The project is done being planned and is currently accepting applications |
| RUNNING     | The project is currently running                                          |
| ENDED       | The project has ended as planned                                          |
| STOPPED     | The project has been stopped after it was already released                |
| CANCELED    | The project has been stopped before it was released                       |

## List project ids</br>![http-get] /projects
Returns a list of all known project ids.

## Get project</br>![http-get] /projects/[{project.id}](#project-object)
Returns a single [project object](#project-object).

## ![http-post] /projects
Create a new project.
Returns the new [project object](#project-object) on success.

## Add project member</br>![http-put] /projects/[{project.id}](#project-object)/members/[{user.id}](User.md#user-object)
Adds a user to a project.
Returns `204 No Content` on success.

## Remove project member</br>![http-delete] /projects/[{project.id}](#project-object)/members/[{user.id}](User.md#user-object)
Removes a user from a project.
Returns `204 No Content` on success.

## Delete project</br>![http-delete] /projects/[{project.id}](#project-object)
Permanently delete a project, identified by its id.
Returns `204 No Content` on success.

## ![http-patch] /projects/[{project.id}](#project-object)
Modify a project, identified by its id.
Returns the modified [project object](#project-object) on success.