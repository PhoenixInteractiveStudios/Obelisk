CREATE TABLE IF NOT EXISTS `user_discord`(
    `user` BIGINT(20) NOT NULL REFERENCES `users`(`id`),
    `discord` BIGINT(20) NOT NULL UNIQUE REFERENCES `discord`(`id`),
    `index` INT NOT NULL,
    PRIMARY KEY(`user`, `discord`)
);