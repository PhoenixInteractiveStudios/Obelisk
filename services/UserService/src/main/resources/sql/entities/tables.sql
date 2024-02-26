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
