ALTER TABLE work_time_project.work_type ADD "number" int4 NULL;
CREATE INDEX work_type_work_id_idx ON work_time_project.work_type (work_id,"number","type");

CREATE SEQUENCE IF NOT EXISTS work_time_project.work_agreement_request_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


ALTER SEQUENCE work_time_project.work_agreement_request_id_seq
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS work_time_project.work_agreement_request
(
    id bigint NOT NULL DEFAULT nextval('work_time_project.work_agreement_request_id_seq'::regclass),
    timestamp timestamp without time zone,
    nik_name character varying(255) COLLATE pg_catalog."default",
    version character varying(255) COLLATE pg_catalog."default",
    comment character varying(4000) COLLATE pg_catalog."default",
    status character varying(255) COLLATE pg_catalog."default",
    term timestamp without time zone,
    work_id bigint NOT NULL,
   CONSTRAINT work_agreement_request_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS work_time_project.work_agreement_request
    OWNER to postgres;

ALTER SEQUENCE work_time_project.work_agreement_request_id_seq
    OWNED BY work_time_project.work_agreement_request.id;

CREATE INDEX work_agreement_request_work_id_idx ON work_time_project.work_agreement_request (work_id,"version");

CREATE SEQUENCE IF NOT EXISTS work_time_project.work_agreement_response_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


ALTER SEQUENCE work_time_project.work_agreement_response_id_seq
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS work_time_project.work_agreement_response
(
    id bigint NOT NULL DEFAULT nextval('work_time_project.work_agreement_response_id_seq'::regclass),
    timestamp timestamp without time zone,
    nik_name character varying(255) COLLATE pg_catalog."default",
    comment character varying(4000) COLLATE pg_catalog."default",
    status character varying(255) COLLATE pg_catalog."default",
    work_id bigint NOT NULL,
    request_id bigint NOT NULL,
   CONSTRAINT work_agreement_response_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS work_time_project.work_agreement_response
    OWNER to postgres;

ALTER SEQUENCE work_time_project.work_agreement_response_id_seq
    OWNED BY work_time_project.work_agreement_request.id;

CREATE INDEX work_agreement_response_work_id_idx ON work_time_project.work_agreement_response (work_id,request_id,"nik_name");

