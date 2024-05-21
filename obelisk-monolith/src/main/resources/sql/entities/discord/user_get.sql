SELECT `users`.*
FROM `users`
INNER JOIN `user_discord` ON `users`.`id` = `user_discord`.`user`
WHERE `user_discord`.`discord` = ?;