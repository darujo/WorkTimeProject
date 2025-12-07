ALTER TABLE telegram_message.message_receive
    ADD thread_id int4 NULL;
ALTER TABLE telegram_message.message_send
    ADD thread_id int4 NULL;
