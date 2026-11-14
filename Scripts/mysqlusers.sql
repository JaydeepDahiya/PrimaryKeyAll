-- updated to use caching_sha2_password plugin for MySQL 9.0 and removed Flush Privileges command since it is not needed in MySQL 9.0
DROP DATABASE IF EXISTS mysqldatabase;
DROP USER IF EXISTS `myadmin`@`%`;
DROP USER IF EXISTS `myuser`@`%`;
CREATE DATABASE IF NOT EXISTS mysqldatabase CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS `myadmin`@`%` IDENTIFIED WITH caching_sha2_password BY 'password';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW,
CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON `mysqldatabase`.* TO `myadmin`@`%`;
CREATE USER IF NOT EXISTS `myuser`@`%` IDENTIFIED WITH caching_sha2_password BY 'password';
GRANT SELECT, INSERT, UPDATE, DELETE, SHOW VIEW ON `mysqldatabase`.* TO `myuser`@`%`;