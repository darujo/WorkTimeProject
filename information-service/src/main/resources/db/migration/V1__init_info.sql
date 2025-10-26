-- SCHEMA: information


-- DROP SCHEMA IF EXISTS information ;

CREATE SCHEMA IF NOT EXISTS information
    AUTHORIZATION postgres;

-- SEQUENCE: information.massage_information_id_seq

-- DROP SEQUENCE IF EXISTS information.massage_information_id_seq;

CREATE SEQUENCE IF NOT EXISTS information.massage_information_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


ALTER SEQUENCE information.massage_information_id_seq
    OWNER TO postgres;


-- Table: information.massage_information

-- DROP TABLE IF EXISTS information.massage_information;

CREATE TABLE IF NOT EXISTS information.massage_information
(
    id bigint NOT NULL DEFAULT nextval('information.massage_information_id_seq'::regclass),
    author character varying(255) COLLATE pg_catalog."default",
    is_send boolean NOT NULL,
    text character varying(255) COLLATE pg_catalog."default",
    type character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT massage_information_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS information.massage_information
    OWNER to postgres;

ALTER SEQUENCE information.massage_information_id_seq
    OWNED BY information.massage_information.id;

-- SEQUENCE: information.user_send_id_seq

-- DROP SEQUENCE IF EXISTS information.user_send_id_seq;

CREATE SEQUENCE IF NOT EXISTS information.user_send_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


ALTER SEQUENCE information.user_send_id_seq
    OWNER TO postgres;


-- Table: information.user_send

-- DROP TABLE IF EXISTS information.user_send;

CREATE TABLE IF NOT EXISTS information.user_send
(
    id bigint NOT NULL DEFAULT nextval('information.user_send_id_seq'::regclass),
    chat_id character varying(255) COLLATE pg_catalog."default",
    send boolean NOT NULL,
    mes_info_id bigint,
    CONSTRAINT user_send_pkey PRIMARY KEY (id),
    CONSTRAINT fk5pdgtonbm62vw4290nncod3gg FOREIGN KEY (mes_info_id)
        REFERENCES information.massage_information (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS information.user_send
    OWNER to postgres;
ALTER SEQUENCE information.user_send_id_seq
    OWNED BY information.user_send.id;


