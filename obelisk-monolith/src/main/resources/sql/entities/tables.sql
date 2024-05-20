-- USERS

CREATE TABLE IF NOT EXISTS `users`(
    `id` BIGINT(20) NOT NULL PRIMARY KEY,
    `name` TEXT NOT NULL
);


-- DISCORD ACCOUNTS

CREATE TABLE IF NOT EXISTS `discord`(
    `id` BIGINT(20) NOT NULL PRIMARY KEY,
    `snowflake` BIGINT(20) NOT NULL UNIQUE,
    `name` TEXT
);
CREATE TABLE IF NOT EXISTS `user_discord`(
    `user` BIGINT(20) NOT NULL REFERENCES `users`(`id`),
    `discord` BIGINT(20) NOT NULL UNIQUE REFERENCES `discord`(`id`),
    `index` INT NOT NULL,
    PRIMARY KEY(`user`, `discord`)
);


-- MINECRAFT ACCOUNTS

CREATE TABLE IF NOT EXISTS `minecraft`(
    `id` BIGINT(20) NOT NULL PRIMARY KEY,
    `uuid` TEXT NOT NULL UNIQUE,
    `name` TEXT
);
CREATE TABLE IF NOT EXISTS `user_minecraft`(
    `user` BIGINT(20) NOT NULL REFERENCES `users`(`id`),
    `minecraft` BIGINT(20) NOT NULL UNIQUE REFERENCES `minecraft`(`id`),
    `index` INT NOT NULL,
    PRIMARY KEY(`user`, `minecraft`)
);


-- TICKETS

CREATE TABLE IF NOT EXISTS `tickets` (
    `id` BIGINT(20) NOT NULL PRIMARY KEY,
    `title` TEXT,
    `state` TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS `ticket_users` (
    `ticket` BIGINT(20) NOT NULL REFERENCES `tickets`(`id`),
    `user` BIGINT(20) NOT NULL REFERENCES `users`(`id`),
    PRIMARY KEY (`ticket`, `user`)
);


-- PROJECTS

CREATE TABLE IF NOT EXISTS `projects`(
    `id` BIGINT(20) NOT NULL PRIMARY KEY,
    `title` TEXT NOT NULL,
    `state` TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS `project_members` (
    `project` BIGINT(20) NOT NULL REFERENCES `projects`(`id`),
    `member` BIGINT(20) NOT NULL REFERENCES `users`(`id`),
    PRIMARY KEY (`project`, `member`)
);
