CREATE TABLE IF NOT EXISTS `sessions` (
    `id` BIGINT(20) NOT NULL,
    `identity` BIGINT(20) NOT NULL,
    `token` TEXT NOT NULL,
    `expired` BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`identity`) REFERENCES `identities`(`token_id`),
    UNIQUE (`token`)
);