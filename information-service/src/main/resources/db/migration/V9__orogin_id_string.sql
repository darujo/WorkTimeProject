ALTER TABLE information.user_send
    ALTER COLUMN origin_message_id TYPE varchar(255) USING origin_message_id::varchar;
