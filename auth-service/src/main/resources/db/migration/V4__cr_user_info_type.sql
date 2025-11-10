-- SEQUENCE: user_repo.user_info_type_id_seq

-- DROP SEQUENCE IF EXISTS user_repo.user_info_type_id_seq;

CREATE SEQUENCE IF NOT EXISTS user_repo.user_info_type_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


ALTER SEQUENCE user_repo.user_info_type_id_seq
    OWNER TO postgres;

-- Table: user_repo.user_info_type

-- DROP TABLE IF EXISTS user_repo.user_info_type;

CREATE TABLE IF NOT EXISTS user_repo.user_info_type
(
    id bigint NOT NULL DEFAULT nextval('user_repo.user_info_type_id_seq'::regclass),
    code character varying(255) COLLATE pg_catalog."default",
    user_id bigint,
    CONSTRAINT user_info_type_pkey PRIMARY KEY (id),
    CONSTRAINT fkkvabg4m6xybgefahycxdwud8p FOREIGN KEY (user_id)
        REFERENCES user_repo.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS user_repo.user_info_type
    OWNER to postgres;

ALTER SEQUENCE user_repo.user_info_type_id_seq
    OWNED BY user_repo.user_info_type.id;

CREATE UNIQUE INDEX user_info_type_code_idx ON user_repo.user_info_type (code,user_id);