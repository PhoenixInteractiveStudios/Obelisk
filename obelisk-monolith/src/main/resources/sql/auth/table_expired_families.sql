CREATE TABLE IF NOT EXISTS `expired_families` (
    `application` BIGINT(20) NOT NULL,
    `family` INT NOT NULL,
    PRIMARY KEY (`application`, `family`),
    FOREIGN KEY (`application`, `family`) REFERENCES `identities`(`application`, `token_family`)
);