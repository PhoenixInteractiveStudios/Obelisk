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