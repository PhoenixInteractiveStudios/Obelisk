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