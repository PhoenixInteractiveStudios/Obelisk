SELECT `intents`.* FROM `intents`
INNER JOIN `application_intents` ON `intents`.`id` = `application_intents`.`intent`
WHERE `application_intents`.`application` = ?;