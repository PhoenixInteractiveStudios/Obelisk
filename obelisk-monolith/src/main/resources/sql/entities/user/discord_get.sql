SELECT
    `discord`.`id`,
    `discord`.`snowflake`,
    `discord`.`name`,
    `user_discord`.`index`
FROM `discord`
INNER JOIN `user_discord` ON `discord`.`id` = `user_discord`.`discord`
WHERE `user_discord`.`user` = ?
ORDER BY `user_discord`.`index` ASC;