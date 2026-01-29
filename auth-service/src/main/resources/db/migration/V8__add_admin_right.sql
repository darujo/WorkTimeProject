INSERT INTO user_repo.rights
    ("name", "label")
SELECT 'ADMIN_USER',
       'Изменять пользователей в любом проекте'
WHERE NOT EXISTS (SELECT 1
                  FROM user_repo.rights
                  WHERE "name" = 'ADMIN_USER');

UPDATE user_repo.rights
SET id = 9
WHERE "name" = 'ADMIN_USER';

INSERT INTO user_repo.user_rights
    (user_id, right_id)
SELECT 1, 9
WHERE NOT EXISTS (SELECT 1 FROM user_repo.user_rights WHERE user_id = 1 and user_rights.right_id = 9);
