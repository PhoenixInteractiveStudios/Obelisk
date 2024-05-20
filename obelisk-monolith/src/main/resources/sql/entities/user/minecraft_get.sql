SELECT
    `minecraft`.`id`,
    `minecraft`.`uuid`,
    `minecraft`.`name`,
    `user_minecraft`.`index`
FROM `minecraft`
INNER JOIN `user_minecraft` ON `minecraft`.`id` = `user_minecraft`.`minecraft`
WHERE `user_minecraft`.`user` = ?
ORDER BY `user_minecraft`.`index` ASC;