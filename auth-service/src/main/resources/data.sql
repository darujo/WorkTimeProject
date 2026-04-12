UPDATE user_repo."users"
SET telegram_id = null
WHERE telegram_id != '496071536';

UPDATE user_repo."user_info_type"
SET telegram_id = null,
    thread_id   = null
WHERE telegram_id != '496071536';
