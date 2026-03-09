CREATE SEQUENCE IF NOT EXISTS work_time_project.release_project_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


ALTER SEQUENCE work_time_project.release_project_id_seq
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS work_time_project.release_project
(
    id                   bigint NOT NULL DEFAULT nextval('work_time_project.release_project_id_seq'::regclass),
    issuing_release_fact timestamp without time zone,
    release_id           bigint NOT NULL,
    project_id           bigint NOT NULL,
    CONSTRAINT release_project_pkey PRIMARY KEY (id)
)
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS work_time_project.release_project
    OWNER to postgres;

ALTER SEQUENCE work_time_project.release_project_id_seq
    OWNED BY work_time_project.release_project.id;

CREATE INDEX release_project_project_idx ON work_time_project."release_project" (release_id, project_id);

INSERT INTO work_time_project.release_project (issuing_release_fact, release_id, project_id)
SELECT r.issuing_release_fact,
       r.id,
       r.project_id
FROM work_time_project.release r;

ALTER TABLE IF EXISTS work_time_project."release"
    DROP COLUMN issuing_release_fact,
    DROP COLUMN project_id;
 
