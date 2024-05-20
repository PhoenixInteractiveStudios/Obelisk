CREATE TABLE IF NOT EXISTS `ticket_users` (
    `ticket` BIGINT(20) NOT NULL REFERENCES `tickets`(`id`),
    `user` BIGINT(20) NOT NULL REFERENCES `users`(`id`),
    PRIMARY KEY (`ticket`, `user`)
);