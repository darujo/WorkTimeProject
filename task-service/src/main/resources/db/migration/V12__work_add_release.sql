ALTER TABLE IF EXISTS work_time_project."work"
    DROP COLUMN analise_end_fact,
    DROP COLUMN analise_end_plan,
    DROP COLUMN analise_start_fact,
    DROP COLUMN analise_start_plan,
    DROP COLUMN debug_end_fact,
    DROP COLUMN debug_end_plan,
    DROP COLUMN debug_start_fact,
    DROP COLUMN debug_start_plan,
    DROP COLUMN develop_end_fact,
    DROP COLUMN develop_end_plan,
    DROP COLUMN develop_start_fact,
    DROP COLUMN develop_start_plan,
    DROP COLUMN issue_prototype_fact,
    DROP COLUMN issue_prototype_plan,
    DROP COLUMN ope_end_fact,
    DROP COLUMN ope_end_plan,
    DROP COLUMN ope_start_fact,
    DROP COLUMN ope_start_plan,
    DROP COLUMN rated,
    DROP COLUMN release_end_fact,
    DROP COLUMN release_end_plan,
    DROP COLUMN release_start_fact,
    DROP COLUMN release_start_plan,
    DROP COLUMN stage_zi,
    DROP COLUMN start_task_fact,
    DROP COLUMN start_task_plan,
    DROP COLUMN task,
    DROP COLUMN laborope,
    DROP COLUMN labor_develop,
    DROP COLUMN labor_debug,
    DROP COLUMN labor_release,
    DROP COLUMN fact_date_stage0,
    DROP COLUMN plan_date_stage0;



UPDATE work_time_project."work" w
SET release_id = (SELECT wp.release_id
                  FROM work_time_project.work_project wp
                  where wp.project_id = 1
                    and wp.work_id = w.id)
WHERE true;

ALTER TABLE IF EXISTS work_time_project."work_project"
    DROP COLUMN release_id;

ALTER TABLE IF EXISTS work_time_project.work
    ADD COLUMN parent_id bigint;