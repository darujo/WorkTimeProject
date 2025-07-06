
ALTER SEQUENCE IF EXISTS work_time_project.workcriteria_id_seq
    RENAME TO work_criteria_id_seq;

ALTER SEQUENCE IF EXISTS work_time_project.workstage_id_seq
    RENAME TO work_stage_id_seq;

ALTER SEQUENCE IF EXISTS work_time_project.worktype_id_seq
    RENAME TO work_type_id_seq;

ALTER TABLE IF EXISTS work_time_project.workcriteria
    RENAME TO work_criteria;

ALTER TABLE IF EXISTS work_time_project.workstage
    RENAME TO work_stage;

ALTER TABLE IF EXISTS work_time_project.worktype
    RENAME TO work_type;

