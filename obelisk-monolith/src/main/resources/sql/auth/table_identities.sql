CREATE TABLE IF NOT EXISTS `identities` (
    `application` BIGINT(20) NOT NULL,
    `token_family` INT NOT NULL,
    `token_id` BIGINT(20) NOT NULL,
    PRIMARY KEY (`token_id`)
);