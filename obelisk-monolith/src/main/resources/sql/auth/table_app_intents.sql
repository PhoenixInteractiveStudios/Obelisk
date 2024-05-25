CREATE TABLE IF NOT EXISTS `application_intents`(
    `application` BIGINT(20) NOT NULL REFERENCES `applications`(`id`),
    `intent` BIGINT(20) NOT NULL REFERENCES `intents`(`id`),
    PRIMARY KEY (`application`, `intent`)
);