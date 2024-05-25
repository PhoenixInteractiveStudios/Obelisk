CREATE TABLE IF NOT EXISTS `applications`(
    `id` BIGINT(20) NOT NULL PRIMARY KEY,
    `name` TEXT NOT NULL,
    `pub_key` TEXT NOT NULL
);