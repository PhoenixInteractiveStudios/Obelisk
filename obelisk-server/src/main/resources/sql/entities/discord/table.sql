CREATE TABLE IF NOT EXISTS `discord`(
    `snowflake` BIGINT(20) NOT NULL PRIMARY KEY,
    `user` BIGINT(20),
    `name` TEXT
);
