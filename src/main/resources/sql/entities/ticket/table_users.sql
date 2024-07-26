CREATE TABLE IF NOT EXISTS `ticket_users` (
    `ticket` INTEGER NOT NULL,
    `user` BIGINT(20) NOT NULL,
    PRIMARY KEY(`ticket`, `user`)
);
