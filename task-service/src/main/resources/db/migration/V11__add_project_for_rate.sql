ALTER TABLE work_time_project."work_type"
    ADD project_id int8;
ALTER TABLE work_time_project."work_stage"
    ADD project_id int8;
ALTER TABLE work_time_project."work_criteria"
    ADD project_id int8;

UPDATE work_time_project."work_type"
SET project_id = 1
WHERE true;

UPDATE work_time_project."work_stage"
SET project_id = 1
WHERE true;

UPDATE work_time_project."work_criteria"
SET project_id = 1
WHERE true;


ALTER TABLE work_time_project."work_type"
    ALTER COLUMN "project_id" SET NOT NULL;
ALTER TABLE work_time_project."work_stage"
    ALTER COLUMN "project_id" SET NOT NULL;
ALTER TABLE work_time_project."work_criteria"
    ALTER COLUMN "project_id" SET NOT NULL;




