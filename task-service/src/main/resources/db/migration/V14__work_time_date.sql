ALTER TABLE work_time_project.work_time
    ALTER COLUMN work_date TYPE date USING work_date::date;

ALTER TABLE work_time_project.task
    ALTER COLUMN time_create TYPE timestamp(6) USING time_create::timestamp,
    ALTER COLUMN "refresh" TYPE timestamp(6) USING "refresh"::timestamp;

ALTER TABLE work_time_project."release"
    ALTER COLUMN issuing_release_plan TYPE date USING issuing_release_plan::date;

ALTER TABLE work_time_project.release_project
    ALTER COLUMN issuing_release_fact TYPE date USING issuing_release_fact::date;

ALTER TABLE work_time_project.work_project
    ALTER COLUMN analise_end_fact TYPE date USING analise_end_fact::date,
    ALTER COLUMN analise_end_plan TYPE date USING analise_end_plan::date,
    ALTER COLUMN analise_start_fact TYPE date USING analise_start_fact::date,
    ALTER COLUMN analise_start_plan TYPE date USING analise_start_plan::date,
    ALTER COLUMN debug_end_fact TYPE date USING debug_end_fact::date,
    ALTER COLUMN debug_end_plan TYPE date USING debug_end_plan::date,
    ALTER COLUMN debug_start_fact TYPE date USING debug_start_fact::date,
    ALTER COLUMN debug_start_plan TYPE date USING debug_start_plan::date,

    ALTER COLUMN develop_end_fact TYPE date USING develop_end_fact::date,
    ALTER COLUMN develop_end_plan TYPE date USING develop_end_plan::date,
    ALTER COLUMN develop_start_fact TYPE date USING develop_start_fact::date,
    ALTER COLUMN develop_start_plan TYPE date USING develop_start_plan::date,
    ALTER COLUMN issue_prototype_fact TYPE date USING issue_prototype_fact::date,
    ALTER COLUMN issue_prototype_plan TYPE date USING issue_prototype_plan::date,

    ALTER COLUMN ope_end_fact TYPE date USING ope_end_fact::date,
    ALTER COLUMN ope_end_plan TYPE date USING ope_end_plan::date,
    ALTER COLUMN ope_start_fact TYPE date USING ope_start_fact::date,
    ALTER COLUMN ope_start_plan TYPE date USING ope_start_plan::date,
    ALTER COLUMN release_end_fact TYPE date USING release_end_fact::date,
    ALTER COLUMN release_end_plan TYPE date USING release_end_plan::date,
    ALTER COLUMN release_start_fact TYPE date USING release_start_fact::date,
    ALTER COLUMN release_start_plan TYPE date USING release_start_plan::date,

    ALTER COLUMN start_task_fact TYPE date USING start_task_fact::date,
    ALTER COLUMN start_task_plan TYPE date USING start_task_plan::date;

ALTER TABLE work_time_project.work_agreement_request
    ALTER COLUMN term TYPE date USING term::date,
    ALTER COLUMN "timestamp" TYPE timestamp(6) USING "timestamp"::timestamp;

ALTER TABLE work_time_project.work_agreement_response
    ALTER COLUMN "timestamp" TYPE timestamp(6) USING "timestamp"::timestamp;

