INSERT INTO user_repo.rights
    ("name", "label")
SELECT 'CATEGORY_AMOUNT',
       'Просмотр суммы по людям.'
WHERE NOT EXISTS (SELECT 1
                  FROM user_repo.rights
                  WHERE "name" = 'CATEGORY_AMOUNT');

INSERT INTO user_repo.rights
    ("name", "label")
SELECT 'CATEGORY_EDIT',
       'Редактирование категорий.'
WHERE NOT EXISTS (SELECT 1
                  FROM user_repo.rights
                  WHERE "name" = 'CATEGORY_EDIT');