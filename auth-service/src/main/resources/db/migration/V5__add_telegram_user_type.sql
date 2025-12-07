ALTER TABLE user_repo.user_info_type
    ADD telegram_id int8 NULL;
ALTER TABLE user_repo.user_info_type
    ADD thread_id int4 NULL;
ALTER TABLE user_repo.user_info_type
    ADD is_active bool NULL;
Set is_active