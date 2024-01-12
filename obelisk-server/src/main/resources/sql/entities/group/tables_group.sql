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