SELECT `users`.*
FROM `users`
INNER JOIN `ticket_users` ON `users`.`id` = `ticket_users`.`user`
WHERE `ticket_users`.`ticket` = ?;