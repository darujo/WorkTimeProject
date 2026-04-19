ALTER TABLE IF EXISTS user_repo.users
    ADD COLUMN code_email character varying(255) COLLATE pg_catalog."default";
ALTER TABLE IF EXISTS user_repo.users
    ADD COLUMN email character varying(255) COLLATE pg_catalog."default";
ALTER TABLE IF EXISTS user_repo.users
    ADD COLUMN email_new character varying(255) COLLATE pg_catalog."default";
ALTER TABLE IF EXISTS user_repo.users
    ADD COLUMN send_code timestamp;
ALTER TABLE IF EXISTS user_repo.users
    ADD COLUMN recovery boolean;