CREATE TABLE IF NOT EXISTS `tickets` (
    `id` BIGINT(20) NOT NULL PRIMARY KEY,
    `title` TEXT,
    `state` TEXT NOT NULL
);