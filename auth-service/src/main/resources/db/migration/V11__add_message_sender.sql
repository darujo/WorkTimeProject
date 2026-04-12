ALTER TABLE user_repo.user_info_type
    ADD COLUMN "sender_type" character varying(255) COLLATE pg_catalog."default";
ALTER TABLE user_repo.user_info_type
    ALTER COLUMN telegram_id TYPE varchar(255) USING telegram_id::varchar;
ALTER TABLE user_repo.users
    ALTER COLUMN telegram_id TYPE varchar(255) USING telegram_id::varchar;

UPDATE user_repo.user_info_type
SET sender_type = 'Telegram'
WHERE true;

ALTER TABLE user_repo.user_info_type
    ALTER COLUMN "sender_type" SET NOT NULL;
