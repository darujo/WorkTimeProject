-- SEQUENCE: user_repo.rights_id_seq

-- DROP SEQUENCE IF EXISTS user_repo.rights_id_seq;

CREATE SEQUENCE IF NOT EXISTS user_repo.rights_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE user_repo.rights_id_seq
    OWNER TO postgres;


-- SEQUENCE: user_repo.roles_id_seq

-- DROP SEQUENCE IF EXISTS user_repo.roles_id_seq;

CREATE SEQUENCE IF NOT EXISTS user_repo.roles_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE user_repo.roles_id_seq
    OWNER TO postgres;

-- SEQUENCE: user_repo.users_id_seq

-- DROP SEQUENCE IF EXISTS user_repo.users_id_seq;

CREATE SEQUENCE IF NOT EXISTS user_repo.users_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE user_repo.users_id_seq
    OWNER TO postgres;


-- SEQUENCE: user_repo.vacation_id_seq

-- DROP SEQUENCE IF EXISTS user_repo.vacation_id_seq;

CREATE SEQUENCE IF NOT EXISTS user_repo.vacation_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE user_repo.vacation_id_seq
    OWNER TO postgres;

-- Table: user_repo.rights

-- DROP TABLE IF EXISTS user_repo.rights;

CREATE TABLE IF NOT EXISTS user_repo.rights
(
    id bigint NOT NULL DEFAULT nextval('user_repo.rights_id_seq'::regclass),
    name character varying(255) COLLATE pg_catalog."default",
    label character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT rights_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS user_repo.rights
    OWNER to postgres;

-- Table: user_repo.roles

-- DROP TABLE IF EXISTS user_repo.roles;

CREATE TABLE IF NOT EXISTS user_repo.roles
(
    id bigint NOT NULL DEFAULT nextval('user_repo.roles_id_seq'::regclass),
    name character varying(255) COLLATE pg_catalog."default",
    label character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT roles_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS user_repo.roles
    OWNER to postgres;

-- Table: user_repo.users

-- DROP TABLE IF EXISTS user_repo.users;

CREATE TABLE IF NOT EXISTS user_repo.users
(
    id bigint NOT NULL DEFAULT nextval('user_repo.users_id_seq'::regclass),
    nik_name character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default",
    first_name character varying(255) COLLATE pg_catalog."default",
    last_name character varying(255) COLLATE pg_catalog."default",
    patronymic character varying(255) COLLATE pg_catalog."default",
    password_change boolean,
    CONSTRAINT users_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS user_repo.users
    OWNER to postgres;

-- Table: user_repo.vacation

-- DROP TABLE IF EXISTS user_repo.vacation;

CREATE TABLE IF NOT EXISTS user_repo.vacation
(
    id bigint NOT NULL DEFAULT nextval('user_repo.vacation_id_seq'::regclass),
    date_end timestamp without time zone,
    date_start timestamp without time zone,
    nik_name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT vacation_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS user_repo.vacation
    OWNER to postgres;

-- Table: user_repo.role_rights

-- DROP TABLE IF EXISTS user_repo.role_rights;

CREATE TABLE IF NOT EXISTS user_repo.role_rights
(
    role_id bigint NOT NULL,
    right_id bigint NOT NULL,
    CONSTRAINT fk41ne3apjkaewtp51db33uu65q FOREIGN KEY (right_id)
        REFERENCES user_repo.rights (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkkcshvt1brg4jjdxxm0xt8mkkb FOREIGN KEY (role_id)
        REFERENCES user_repo.roles (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS user_repo.role_rights
    OWNER to postgres;

-- Table: user_repo.user_rights

-- DROP TABLE IF EXISTS user_repo.user_rights;

CREATE TABLE IF NOT EXISTS user_repo.user_rights
(
    user_id bigint NOT NULL,
    right_id bigint NOT NULL,
    CONSTRAINT fk33bady8c7jrw9flq6dg082bkc FOREIGN KEY (right_id)
        REFERENCES user_repo.rights (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkf4ougno3bxa9w57mjxldwsow3 FOREIGN KEY (user_id)
        REFERENCES user_repo.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS user_repo.user_rights
    OWNER to postgres;

-- Table: user_repo.user_roles

-- DROP TABLE IF EXISTS user_repo.user_roles;

CREATE TABLE IF NOT EXISTS user_repo.user_roles
(
    user_id bigint NOT NULL,
    role_id bigint NOT NULL,
    CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id)
        REFERENCES user_repo.roles (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id)
        REFERENCES user_repo.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS user_repo.user_roles
    OWNER to postgres;




ALTER SEQUENCE user_repo.rights_id_seq
    OWNED BY user_repo.rights.id;

ALTER SEQUENCE user_repo.roles_id_seq
    OWNED BY user_repo.roles.id;

ALTER SEQUENCE user_repo.users_id_seq
    OWNED BY user_repo.users.id;

ALTER SEQUENCE user_repo.vacation_id_seq
    OWNED BY user_repo.vacation.id;

