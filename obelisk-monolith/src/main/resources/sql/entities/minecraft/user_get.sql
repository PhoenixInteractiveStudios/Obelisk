SELECT `users`.*
FROM `users`
INNER JOIN `user_minecraft` ON `users`.`id` = `user_minecraft`.`user`
WHERE `user_minecraft`.`minecraft` = ?;