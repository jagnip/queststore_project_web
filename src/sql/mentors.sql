BEGIN TRANSACTION;
DROP TABLE IF EXISTS `mentors`;
CREATE TABLE IF NOT EXISTS `mentors` (
	`name`	TEXT NOT NULL,
	`surname`	TEXT NOT NULL,
	`password`	TEXT NOT NULL,
	`login`	TEXT NOT NULL,
	`mail`	TEXT NOT NULL
);
COMMIT;
