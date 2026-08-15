ALTER TABLE user_repo."vacation"
    ADD type    varchar(255) NULL,
    ADD dynamic boolean      NULL;

UPDATE user_repo."vacation"
SET type    = 'VACATION',
    dynamic = false
WHERE true;

ALTER TABLE user_repo."vacation"
    ALTER COLUMN "type" SET NOT NULL,
    ALTER COLUMN "dynamic" SET NOT NULL;


