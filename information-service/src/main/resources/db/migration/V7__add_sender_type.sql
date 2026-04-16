ALTER TABLE information.user_send
    ADD COLUMN "sender_type" character varying(255) COLLATE pg_catalog."default";
UPDATE information.user_send
SET sender_type = 'Telegram'
WHERE true;

ALTER TABLE information.user_send
    ALTER COLUMN "sender_type" SET NOT NULL;