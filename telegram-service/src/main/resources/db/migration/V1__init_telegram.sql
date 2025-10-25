-- SCHEMA: telegram_message

-- DROP SCHEMA IF EXISTS telegram_message ;

CREATE SCHEMA IF NOT EXISTS telegram_message
    AUTHORIZATION postgres;

-- SEQUENCE: telegram_message.message_receive_id_seq

-- DROP SEQUENCE IF EXISTS telegram_message.message_receive_id_seq;

CREATE SEQUENCE IF NOT EXISTS telegram_message.message_receive_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


ALTER SEQUENCE telegram_message.message_receive_id_seq
    OWNER TO postgres;

-- Table: telegram_message.message_receive

-- DROP TABLE IF EXISTS telegram_message.message_receive;

CREATE TABLE IF NOT EXISTS telegram_message.message_receive
(
    id bigint NOT NULL DEFAULT nextval('telegram_message.message_receive_id_seq'::regclass),
    chat_id bigint,
    first_name character varying(255) COLLATE pg_catalog."default",
    is_channel_chat boolean,
    is_forum boolean,
    is_group_chat boolean,
    is_super_group_chat boolean,
    is_user_char boolean,
    last_name character varying(255) COLLATE pg_catalog."default",
    text character varying(255) COLLATE pg_catalog."default",
    title character varying(255) COLLATE pg_catalog."default",
    type character varying(255) COLLATE pg_catalog."default",
    user_name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT message_receive_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;
ALTER SEQUENCE telegram_message.message_receive_id_seq
    OWNED BY telegram_message.message_receive.id;

ALTER TABLE IF EXISTS telegram_message.message_receive
    OWNER to postgres;


-- SEQUENCE: telegram_message.message_send_id_seq

-- DROP SEQUENCE IF EXISTS telegram_message.message_send_id_seq;

CREATE SEQUENCE IF NOT EXISTS telegram_message.message_send_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


ALTER SEQUENCE telegram_message.message_send_id_seq
    OWNER TO postgres;
-- Table: telegram_message.message_send

-- DROP TABLE IF EXISTS telegram_message.message_send;

CREATE TABLE IF NOT EXISTS telegram_message.message_send
(
    id bigint NOT NULL DEFAULT nextval('telegram_message.message_send_id_seq'::regclass),
    author character varying(255) COLLATE pg_catalog."default",
    chat_id character varying(255) COLLATE pg_catalog."default",
    text character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT message_send_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS telegram_message.message_send
    OWNER to postgres;

ALTER SEQUENCE telegram_message.message_send_id_seq
    OWNED BY telegram_message.message_send.id;
