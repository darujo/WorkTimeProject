INSERT INTO userrepo.rights
("id","name","label")
VALUES
(1,'ZI_EDIT','ЗИ изменение'),
(2,'TASK_EDIT','Задачи изменение'),
(3,'WORK_TIME_EDIT','Работы изменение'),
(4,'ZI_CREATE','ЗИ создание'),
(5,'TASK_CREATE','Задачи создание'),
(6,'WORK_TIME_CREATE','Работы создание'),
(7,'WORK_TIME_CHANGE_USER','Работы изменение исполнителя'),
(8,'EDIT_USER','Пользователи и группы редактирование');

INSERT INTO userrepo.roles
(id, "name","label")
VALUES(1,'ADMIN','Администраторы');

INSERT INTO userrepo.role_rights
(role_id, right_id)
VALUES(1, 1);
-- пароль 1
INSERT INTO userrepo.users (id,nik_name, userpasword, first_name, last_name, patronymic)
VALUES (1,'ADMIN', '$2a$12$s/dfgSfPDZ8.VzJsNWyc2.HtONZFFITs1abzNX8Bei4ebLmnwOkDO', 'Администратор', 'Администратор','Администратор');

INSERT INTO userrepo.user_roles
(user_id, role_id)
VALUES(1, 1);