CREATE TABLE IF NOT EXISTS `tickets` (
    `id` INTEGER PRIMARY KEY,
    `channel` BIGINT(20) NOT NULL UNIQUE,
    `title` TEXT
);
