CREATE TABLE IF NOT EXISTS `expired_families` (
    `subject` BIGINT(20) NOT NULL,
    `family` INT NOT NULL,
    PRIMARY KEY (`subject`, `family`),
    FOREIGN KEY (`subject`, `family`) REFERENCES `identities`(`subject`, `token_family`)
);