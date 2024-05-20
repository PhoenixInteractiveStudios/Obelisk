CREATE TABLE IF NOT EXISTS `project_members` (
    `project` BIGINT(20) NOT NULL REFERENCES `projects`(`id`),
    `member` BIGINT(20) NOT NULL REFERENCES `users`(`id`),
    PRIMARY KEY (`project`, `member`)
);
