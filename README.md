[license]: LICENSE
[license-shield]: https://img.shields.io/badge/License-MIT-yellow.svg
[release]: https://github.com/BurrowStudios/Obelisk/releases
[release-shield]: https://img.shields.io/github/release/BurrowStudios/Obelisk.svg

[![license-shield][]][license]
[![release-shield][]][release]

# Obelisk

Obelisk is the Burrow Studios API. This repository servers as a monorepo for all projects that form the backend and any
API clients.

### Projects

[build-commons]: https://github.com/BurrowStudios/Obelisk/actions/workflows/build-commons.yaml
[build-commons-shield]: https://img.shields.io/github/actions/workflow/status/BurrowStudios/Obelisk/build-commons.yaml
[build-commons-http]: https://github.com/BurrowStudios/Obelisk/actions/workflows/build-commons-http.yaml
[build-commons-http-shield]: https://img.shields.io/github/actions/workflow/status/BurrowStudios/Obelisk/build-commons-http.yaml
[service-build-authentication]: https://github.com/BurrowStudios/Obelisk/actions/workflows/service-build-authentication.yaml
[service-build-authentication-shield]: https://img.shields.io/github/actions/workflow/status/BurrowStudios/Obelisk/service-build-authentication.yaml
[service-build-authorization]: https://github.com/BurrowStudios/Obelisk/actions/workflows/service-build-authorization.yaml
[service-build-authorization-shield]: https://img.shields.io/github/actions/workflow/status/BurrowStudios/Obelisk/service-build-authorization.yaml
[service-build-IssueTracker]: https://github.com/BurrowStudios/Obelisk/actions/workflows/service-build-IssueTracker.yaml
[service-build-IssueTracker-shield]: https://img.shields.io/github/actions/workflow/status/BurrowStudios/Obelisk/service-build-IssueTracker.yaml
[service-build-UserService]: https://github.com/BurrowStudios/Obelisk/actions/workflows/service-build-UserService.yaml
[service-build-UserService-shield]: https://img.shields.io/github/actions/workflow/status/BurrowStudios/Obelisk/service-build-UserService.yaml
[build-api]: https://github.com/BurrowStudios/Obelisk/actions/workflows/build-api.yaml
[build-api-shield]: https://img.shields.io/github/actions/workflow/status/BurrowStudios/Obelisk/build-api.yaml
[build-server]: https://github.com/BurrowStudios/Obelisk/actions/workflows/build-server.yaml
[build-server-shield]: https://img.shields.io/github/actions/workflow/status/BurrowStudios/Obelisk/build-server.yaml

| Project         | Layer                                          | Relative Path                                        | Status                                                                   | Description                                                                         |
|-----------------|------------------------------------------------|------------------------------------------------------|--------------------------------------------------------------------------|-------------------------------------------------------------------------------------|
| Commons Library |                                                | [`obelisk-commons/`](obelisk-commons)                | [![build-commons-shield][]][build-commons]                               | Some utilities commonly used in multiple Obelisk services and components            |
| Message Broker  | Communication                                  | [`message-broker/`](message-broker)                  | Incomplete                                                               | Template setup for the [RabbitMQ](https://www.rabbitmq.com/) backend message broker |
| Shelly          | Interface                                      | [`services/authentication`](services/authentication) | [![service-build-authentication-shield][]][service-build-authentication] | Authentication service                                                              |
| Chramel         | Interface                                      | [`services/authorization`](services/authorization)   | [![service-build-authorization-shield][]][service-build-authorization]   | Authorization service                                                               |
| Bruno           | Service                                        | [`services/IssueTracker`](services/IssueTracker)     | [![service-build-IssueTracker-shield][]][service-build-IssueTracker]     | Issue-Tracker-System (ITS)                                                          |
| UserService     | Service                                        | [`services/UserService`](services/UserService)       | [![service-build-UserService-shield][]][service-build-UserService]       | User & Group management                                                             |
| API client      | Application                                    | [`obelisk-api/`](obelisk-api)                        | [![build-api-shield][]][build-api]                                       | Burrow Studios API client library                                                   |

### Architecture

The repository is currently still in the process of migrating into a monorepo (splitting up modules into separate
projects) and thus, the architecture is still a bit fuzzy. This is the current plan for a less monolithic architecture:

![](res/architecture.png)

### ⚠️ Early development notice ⚠️

Please note that this application is still in early development and not officially supported until the first release.
