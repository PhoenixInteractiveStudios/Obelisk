CREATE TABLE IF NOT EXISTS `projects` (
    `id` INTEGER PRIMARY KEY,
    `title` TEXT NOT NULL,
    `application_template` TEXT,
    `invite_only` BOOLEAN NOT NULL,
);
