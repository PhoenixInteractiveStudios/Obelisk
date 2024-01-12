CREATE TABLE IF NOT EXISTS `projects` (
    `id` BIGINT(20) NOT NULL,
    `title` TEXT NOT NULL,
    `state` TEXT NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `project_timings` (
    `project` BIGINT(20) NOT NULL,
    `name` VARCHAR(256) NOT NULL,
    `time` TIMESTAMP NOT NULL,
    PRIMARY KEY (`project`, `name`)
);
CREATE TABLE IF NOT EXISTS `project_members` (
    `project` BIGINT(20) NOT NULL,
    `member` BIGINT(20) NOT NULL,
    PRIMARY KEY (`project`, `member`)
);
ALTER TABLE `project_timings` ADD FOREIGN KEY (`project`) REFERENCES `projects`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `project_members` ADD FOREIGN KEY (`project`) REFERENCES `projects`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;