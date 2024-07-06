CREATE TABLE IF NOT EXISTS `project_members` (
    `project` INTEGER NOT NULL,
    `user` BIGINT(20) NOT NULL,
    PRIMARY KEY(`project`, `user`)
);
