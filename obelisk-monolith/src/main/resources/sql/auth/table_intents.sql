CREATE TABLE IF NOT EXISTS `intents`(
    `id` BIGINT(20) NOT NULL PRIMARY KEY,
    `name` TEXT NOT NULL UNIQUE,
    `description` TEXT
);