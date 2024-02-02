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

[build-common]: https://github.com/BurrowStudios/Obelisk/actions/workflows/build-common.yaml
[build-common-shield]: https://img.shields.io/github/actions/workflow/status/BurrowStudios/Obelisk/build-common.yaml
[build-api]: https://github.com/BurrowStudios/Obelisk/actions/workflows/build-api.yaml
[build-api-shield]: https://img.shields.io/github/actions/workflow/status/BurrowStudios/Obelisk/build-api.yaml
[build-server]: https://github.com/BurrowStudios/Obelisk/actions/workflows/build-server.yaml
[build-server-shield]: https://img.shields.io/github/actions/workflow/status/BurrowStudios/Obelisk/build-server.yaml

| Project         | Relative Path                       | Status                                   | Description                                                              |
|-----------------|-------------------------------------|------------------------------------------|--------------------------------------------------------------------------|
| Commons Library | [`obelisk-api/`](obelisk-common)    | [![build-common-shield][]][build-common] | Some utilities commonly used in multiple Obelisk services and components |
| API client      | [`obelisk-api/`](obelisk-api)       | [![build-api-shield][]][build-api]       | Burrow Studios API client library                                        |
| Server          | [`obelisk-server/`](obelisk-server) | [![build-server-shield][]][build-server] | Monolithic backend server (legacy)                                       |

### ⚠️ Early development notice ⚠️

Please note that this application is still in early development and not officially supported until the first release.
