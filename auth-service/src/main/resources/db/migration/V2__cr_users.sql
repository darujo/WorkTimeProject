INSERT INTO user_repo.rights
("name","label")
SELECT
'ZI_EDIT','ЗИ изменение'
WHERE NOT EXISTS (
    SELECT 1 FROM user_repo.rights WHERE "name" = 'ZI_EDIT');

UPDATE user_repo.rights
SET id = 1
WHERE "name" = 'ZI_EDIT';


INSERT INTO user_repo.rights
("name","label")
SELECT
'TASK_EDIT','Задачи изменение'
WHERE NOT EXISTS (
    SELECT 1 FROM user_repo.rights WHERE "name" = 'TASK_EDIT');

UPDATE user_repo.rights
SET id = 2
WHERE "name" = 'TASK_EDIT';



INSERT INTO user_repo.rights
("name","label")
SELECT
'WORK_TIME_EDIT','Работы изменение'
WHERE NOT EXISTS (
    SELECT 1 FROM user_repo.rights WHERE "name" = 'WORK_TIME_EDIT');

UPDATE user_repo.rights
SET id = 3
WHERE "name" = 'WORK_TIME_EDIT';


INSERT INTO user_repo.rights
("name","label")
SELECT
'ZI_CREATE','ЗИ создание'
WHERE NOT EXISTS (
    SELECT 1 FROM user_repo.rights WHERE "name" = 'ZI_CREATE');

UPDATE user_repo.rights
SET id = 4
WHERE "name" = 'ZI_CREATE';


INSERT INTO user_repo.rights
("name","label")
SELECT
'TASK_CREATE','Задачи создание'
WHERE NOT EXISTS (
    SELECT 1 FROM user_repo.rights WHERE "name" = 'TASK_CREATE');

UPDATE user_repo.rights
SET id = 5
WHERE "name" = 'TASK_CREATE';



INSERT INTO user_repo.rights
("name","label")
SELECT
'WORK_TIME_CREATE','Работы создание'
WHERE NOT EXISTS (
    SELECT 1 FROM user_repo.rights WHERE "name" = 'WORK_TIME_CREATE');

UPDATE user_repo.rights
SET id = 6
WHERE "name" = 'WORK_TIME_CREATE';



INSERT INTO user_repo.rights
("name","label")
SELECT
'WORK_TIME_CHANGE_USER','Работы изменение исполнителя'
WHERE NOT EXISTS (
    SELECT 1 FROM user_repo.rights WHERE "name" = 'WORK_TIME_CHANGE_USER');

UPDATE user_repo.rights
SET id = 7
WHERE "name" = 'WORK_TIME_CHANGE_USER';



INSERT INTO user_repo.rights
("name","label")
SELECT
'EDIT_USER','Пользователи и группы редактирование'
WHERE NOT EXISTS (
    SELECT 1 FROM user_repo.rights WHERE "name" = 'EDIT_USER');

UPDATE user_repo.rights
SET id = 8
WHERE "name" = 'EDIT_USER';


INSERT INTO user_repo.roles
("name","label")
SELECT 'ADMIN','Администраторы' WHERE NOT EXISTS (
    SELECT 1 FROM user_repo.roles WHERE "name" = 'ADMIN');

UPDATE user_repo.roles
SET id = 1
WHERE "name" = 'ADMIN';




INSERT INTO user_repo.role_rights
(role_id, right_id)
SELECT 1, 1 WHERE NOT EXISTS (
    SELECT 1 FROM user_repo.role_rights WHERE role_id = 1 and right_id = 1);


INSERT INTO user_repo.role_rights
(role_id, right_id)
SELECT 1, 2 WHERE NOT EXISTS (
    SELECT 1 FROM user_repo.role_rights WHERE role_id = 1 and right_id = 2);

INSERT INTO user_repo.role_rights
(role_id, right_id)
SELECT 1, 3 WHERE NOT EXISTS (
    SELECT 1 FROM user_repo.role_rights WHERE role_id = 1 and right_id = 3);

INSERT INTO user_repo.role_rights
(role_id, right_id)
SELECT 1, 4 WHERE NOT EXISTS (
    SELECT 1 FROM user_repo.role_rights WHERE role_id = 1 and right_id = 4);

INSERT INTO user_repo.role_rights
(role_id, right_id)
SELECT 1, 5 WHERE NOT EXISTS (
    SELECT 1 FROM user_repo.role_rights WHERE role_id = 1 and right_id = 5);

INSERT INTO user_repo.role_rights
(role_id, right_id)
SELECT 1, 6 WHERE NOT EXISTS (
    SELECT 1 FROM user_repo.role_rights WHERE role_id = 1 and right_id = 6);

INSERT INTO user_repo.role_rights
(role_id, right_id)
SELECT 1, 7 WHERE NOT EXISTS (
    SELECT 1 FROM user_repo.role_rights WHERE role_id = 1 and right_id = 7);

INSERT INTO user_repo.role_rights
(role_id, right_id)
SELECT 1, 8 WHERE NOT EXISTS (
    SELECT 1 FROM user_repo.role_rights WHERE role_id = 1 and right_id = 8);



-- пароль 1
INSERT INTO user_repo.users (nik_name, "password", first_name, last_name, patronymic)
SELECT 'Admin', '$2a$12$s/dfgSfPDZ8.VzJsNWyc2.HtONZFFITs1abzNX8Bei4ebLmnwOkDO', 'Администратор', 'Администратор','Администратор'
WHERE NOT EXISTS (
    SELECT 1 FROM user_repo.users WHERE nik_name = 'Admin');

UPDATE user_repo.users
SET id = 1
WHERE nik_name = 'Admin';

INSERT INTO user_repo.user_roles
(user_id, role_id)
SELECT 1, 1 WHERE NOT EXISTS (
    SELECT 1 FROM user_repo.user_roles WHERE user_id = 1 and role_id = 1);
