ALTER TABLE user_repo.users
    ADD block bool NULL;
UPDATE user_repo.users
SET block = false
WHERE true;

ALTER TABLE user_repo.users
    ALTER COLUMN "block" SET NOT NULL;