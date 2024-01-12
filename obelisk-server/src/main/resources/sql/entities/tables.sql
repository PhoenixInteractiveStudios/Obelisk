-- GROUPS

CREATE TABLE IF NOT EXISTS `groups`(
    `id` BIGINT(20) NOT NULL,
    `name` TEXT NOT NULL,
    `position` INT NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `group_members`(
    `group` BIGINT(20) NOT NULL,
    `user` BIGINT(20) NOT NULL,
    PRIMARY KEY (`group`, `user`)
);
ALTER TABLE `group_members` ADD FOREIGN KEY (`group`) REFERENCES `groups`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;


-- PROJECTS

CREATE TABLE IF NOT EXISTS `projects` (
    `id` BIGINT(20) NOT NULL,
    `title` TEXT NOT NULL,
    `state` TEXT NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `project_timings` (
    `project` BIGINT(20) NOT NULL,
    `name` VARCHAR(256) NOT NULL,
    `time` TIMESTAMP NOT NULL,
    PRIMARY KEY (`project`, `name`)
);
CREATE TABLE IF NOT EXISTS `project_members` (
    `project` BIGINT(20) NOT NULL,
    `member` BIGINT(20) NOT NULL,
    PRIMARY KEY (`project`, `member`)
);
ALTER TABLE `project_timings` ADD FOREIGN KEY (`project`) REFERENCES `projects`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `project_members` ADD FOREIGN KEY (`project`) REFERENCES `projects`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;


-- TICKETS

CREATE TABLE IF NOT EXISTS `tickets` (
    `id` BIGINT(20) NOT NULL,
    `title` TEXT NULL,
    `state` TEXT NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `ticket_tags` (
    `ticket` BIGINT(20) NOT NULL,
    `tag` VARCHAR(256) NOT NULL,
    PRIMARY KEY (`ticket`, `tag`)
);
CREATE TABLE IF NOT EXISTS `ticket_users` (
    `ticket` BIGINT(20) NOT NULL,
    `user` BIGINT(20) NOT NULL,
    PRIMARY KEY (`ticket`, `user`)
);
ALTER TABLE `ticket_tags` ADD FOREIGN KEY (`ticket`) REFERENCES `tickets`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `ticket_users` ADD FOREIGN KEY (`ticket`) REFERENCES `tickets`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;


-- USERS

CREATE TABLE IF NOT EXISTS `users`(
    `id` BIGINT(20) NOT NULL,
    `name` TEXT NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `user_discord`(
    `user` BIGINT(20) NOT NULL,
    `discord` BIGINT(20) NOT NULL,
    PRIMARY KEY (`user`, `discord`),
    UNIQUE (`discord`)
);
CREATE TABLE IF NOT EXISTS `user_minecraft`(
    `user` BIGINT(20) NOT NULL,
    `minecraft` UUID NOT NULL,
    PRIMARY KEY (`user`, `minecraft`),
    UNIQUE (`minecraft`)
);
ALTER TABLE `user_discord` ADD FOREIGN KEY (`user`) REFERENCES `users`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `user_minecraft` ADD FOREIGN KEY (`user`) REFERENCES `users`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;


-- BOARDS

CREATE TABLE IF NOT EXISTS `boards` (
    `id` BIGINT(20) NOT NULL,
    `title` TEXT NOT NULL,
    `group_id` BIGINT(20) NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `issues` (
    `id` BIGINT(20) NOT NULL,
    `board` BIGINT(20) NOT NULL,
    `author` BIGINT(20) NOT NULL,
    `title` TEXT NOT NULL,
    `state` TEXT NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `tags` (
    `id` BIGINT(20) NOT NULL,
    `board` BIGINT(20) NOT NULL,
    `name` TEXT NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `issue_assignees` (
    `issue` BIGINT(20) NOT NULL,
    `user` BIGINT(20) NOT NULL,
    PRIMARY KEY (`issue`, `user`)
);
CREATE TABLE IF NOT EXISTS `issue_tags` (
    `issue` BIGINT(20) NOT NULL,
    `tag` BIGINT(20) NOT NULL,
    PRIMARY KEY (`issue`, `tag`)
);
ALTER TABLE `issues` ADD FOREIGN KEY (`board`) REFERENCES `boards`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `tags` ADD FOREIGN KEY (`board`) REFERENCES `boards`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `issue_tags` ADD FOREIGN KEY (`issue`) REFERENCES `issues`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `issue_tags` ADD FOREIGN KEY (`tag`) REFERENCES `tags`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;