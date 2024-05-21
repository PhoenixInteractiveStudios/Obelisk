SELECT `users`.*
FROM `users`
INNER JOIN `project_members` ON `users`.`id` = `project_members`.`member`
WHERE `project_members`.`project` = ?;