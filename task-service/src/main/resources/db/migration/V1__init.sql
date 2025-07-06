-- SCHEMA: work_time_project

-- DROP SCHEMA IF EXISTS work_time_project ;

CREATE SCHEMA IF NOT EXISTS work_time_project
    AUTHORIZATION postgres;

-- SEQUENCE: work_time_project.release_id_seq

-- DROP SEQUENCE IF EXISTS work_time_project.release_id_seq;

CREATE SEQUENCE IF NOT EXISTS work_time_project.release_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


ALTER SEQUENCE work_time_project.release_id_seq
    OWNER TO postgres;

-- Table: work_time_project.release

-- DROP TABLE IF EXISTS work_time_project.release;

CREATE TABLE IF NOT EXISTS work_time_project.release
(
    id bigint NOT NULL DEFAULT nextval('work_time_project.release_id_seq'::regclass),
    issuing_release_fact timestamp without time zone,
    issuing_release_plan timestamp without time zone,
    name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT release_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS work_time_project.release
    OWNER to postgres;

ALTER SEQUENCE work_time_project.release_id_seq
    OWNED BY work_time_project.release.id;

-- SEQUENCE: work_time_project.task_id_seq

-- DROP SEQUENCE IF EXISTS work_time_project.task_id_seq;

CREATE SEQUENCE IF NOT EXISTS work_time_project.task_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE work_time_project.task_id_seq
    OWNER TO postgres;

-- Table: work_time_project.task

-- DROP TABLE IF EXISTS work_time_project.task;

CREATE TABLE IF NOT EXISTS work_time_project.task
(
    id bigint NOT NULL DEFAULT nextval('work_time_project.task_id_seq'::regclass),
    codebts character varying(255) COLLATE pg_catalog."default",
    codedevbo character varying(255) COLLATE pg_catalog."default",
    description character varying(255) COLLATE pg_catalog."default",
    type integer,
    nik_name character varying(255) COLLATE pg_catalog."default",
    work_id bigint,
    refresh timestamp without time zone,
    time_create timestamp without time zone,
    CONSTRAINT task_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS work_time_project.task
    OWNER to postgres;

ALTER SEQUENCE work_time_project.task_id_seq
    OWNED BY work_time_project.task.id;


-- SEQUENCE: work_time_project.work_id_seq

-- DROP SEQUENCE IF EXISTS work_time_project.work_id_seq;

CREATE SEQUENCE IF NOT EXISTS work_time_project.work_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE work_time_project.work_id_seq
    OWNER TO postgres;

-- Table: work_time_project.work

-- DROP TABLE IF EXISTS work_time_project.work;

CREATE TABLE IF NOT EXISTS work_time_project.work
(
    id bigint NOT NULL DEFAULT nextval('work_time_project.work_id_seq'::regclass),
    code_sap bigint,
    codezi character varying(255) COLLATE pg_catalog."default",
    develop_end_fact timestamp without time zone,
    develop_end_plan timestamp without time zone,
    analise_end_fact timestamp without time zone,
    analise_end_plan timestamp without time zone,
    release_end_fact timestamp without time zone,
    release_end_plan timestamp without time zone,
    debug_end_fact timestamp without time zone,
    debug_end_plan timestamp without time zone,
    ope_end_fact timestamp without time zone,
    ope_end_plan timestamp without time zone,
    description character varying(255) COLLATE pg_catalog."default",
    fact_date_stage0 timestamp without time zone,
    labor_debug real,
    labor_develop real,
    laborope real,
    labor_release real,
    name character varying(255) COLLATE pg_catalog."default",
    plan_date_stage0 timestamp without time zone,
    stagezi integer,
    start_task_fact timestamp without time zone,
    start_task_plan timestamp without time zone,
    task character varying(255) COLLATE pg_catalog."default",
    release_id bigint,
    analise_start_fact timestamp without time zone,
    analise_start_plan timestamp without time zone,
    debug_start_fact timestamp without time zone,
    debug_start_plan timestamp without time zone,
    develop_start_fact timestamp without time zone,
    develop_start_plan timestamp without time zone,
    ope_start_fact timestamp without time zone,
    ope_start_plan timestamp without time zone,
    release_start_fact timestamp without time zone,
    release_start_plan timestamp without time zone,
    CONSTRAINT work_pkey PRIMARY KEY (id),
    CONSTRAINT fk1js4j99iww438o2m0rxiq5y10 FOREIGN KEY (release_id)
        REFERENCES work_time_project.release (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS work_time_project.work
    OWNER to postgres;

ALTER SEQUENCE work_time_project.work_id_seq
    OWNED BY work_time_project.work.id;


-- SEQUENCE: work_time_project.work_time_id_seq

-- DROP SEQUENCE IF EXISTS work_time_project.work_time_id_seq;

CREATE SEQUENCE IF NOT EXISTS work_time_project.work_time_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE work_time_project.work_time_id_seq
    OWNER TO postgres;


-- Table: work_time_project.work_time

-- DROP TABLE IF EXISTS work_time_project.work_time;

CREATE TABLE IF NOT EXISTS work_time_project.work_time
(
    id bigint NOT NULL DEFAULT nextval('work_time_project.work_time_id_seq'::regclass),
    task_id bigint,
    nik_name character varying(255) COLLATE pg_catalog."default",
    work_date timestamp without time zone,
    work_time real,
    comment character varying COLLATE pg_catalog."default",
    type integer,
    CONSTRAINT work_time_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS work_time_project.work_time
    OWNER to postgres;

ALTER SEQUENCE work_time_project.work_time_id_seq
    OWNED BY work_time_project.work_time.id;


-- SEQUENCE: work_time_project.workcriteria_id_seq

-- DROP SEQUENCE IF EXISTS work_time_project.workcriteria_id_seq;

CREATE SEQUENCE IF NOT EXISTS work_time_project.workcriteria_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE work_time_project.workcriteria_id_seq
    OWNER TO postgres;

-- Table: work_time_project.workcriteria

-- DROP TABLE IF EXISTS work_time_project.workcriteria;

CREATE TABLE IF NOT EXISTS work_time_project.workcriteria
(
    id bigint NOT NULL DEFAULT nextval('work_time_project.workcriteria_id_seq'::regclass),
    criteria integer,
    develop10 real,
    develop100 real,
    develop50 real,
    work_id bigint,
    CONSTRAINT workcriteria_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS work_time_project.workcriteria
    OWNER to postgres;

ALTER SEQUENCE work_time_project.workcriteria_id_seq
    OWNED BY work_time_project.workcriteria.id;


-- SEQUENCE: work_time_project.workstage_id_seq

-- DROP SEQUENCE IF EXISTS work_time_project.workstage_id_seq;

CREATE SEQUENCE IF NOT EXISTS work_time_project.workstage_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE work_time_project.workstage_id_seq
    OWNER TO postgres;


-- Table: work_time_project.workstage

-- DROP TABLE IF EXISTS work_time_project.workstage;

CREATE TABLE IF NOT EXISTS work_time_project.workstage
(
    id bigint NOT NULL DEFAULT nextval('work_time_project.workstage_id_seq'::regclass),
    nik_name character varying(255) COLLATE pg_catalog."default",
    role integer,
    stage0 real,
    stage1 real,
    stage2 real,
    stage3 real,
    stage4 real,
    work_id bigint,
    CONSTRAINT workstage_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS work_time_project.workstage
    OWNER to postgres;

ALTER SEQUENCE work_time_project.workstage_id_seq
    OWNED BY work_time_project.workstage.id;


-- SEQUENCE: work_time_project.worktype_id_seq

-- DROP SEQUENCE IF EXISTS work_time_project.worktype_id_seq;

CREATE SEQUENCE IF NOT EXISTS work_time_project.worktype_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE work_time_project.worktype_id_seq
    OWNER TO postgres;


-- Table: work_time_project.worktype

-- DROP TABLE IF EXISTS work_time_project.worktype;

CREATE TABLE IF NOT EXISTS work_time_project.worktype
(
    id bigint NOT NULL DEFAULT nextval('work_time_project.worktype_id_seq'::regclass),
    "time" real,
    type character varying(255) COLLATE pg_catalog."default",
    work_id bigint,
    CONSTRAINT worktype_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS work_time_project.worktype
    OWNER to postgres;

ALTER SEQUENCE work_time_project.worktype_id_seq
    OWNED BY work_time_project.worktype.id;

