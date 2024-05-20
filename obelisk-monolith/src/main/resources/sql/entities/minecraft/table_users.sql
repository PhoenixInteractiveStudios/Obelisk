CREATE TABLE IF NOT EXISTS `user_minecraft`(
    `user` BIGINT(20) NOT NULL REFERENCES `users`(`id`),
    `minecraft` BIGINT(20) NOT NULL UNIQUE REFERENCES `minecraft`(`id`),
    `index` INT NOT NULL,
    PRIMARY KEY(`user`, `minecraft`)
);