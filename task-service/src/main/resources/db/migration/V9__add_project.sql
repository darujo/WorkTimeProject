ALTER TABLE work_time_project."work"
    RENAME COLUMN codezi TO code_zi;
ALTER TABLE work_time_project."work"
    RENAME COLUMN stagezi TO stage_zi;
ALTER TABLE work_time_project.task
    RENAME COLUMN codebts TO code_bts;
ALTER TABLE work_time_project.task
    RENAME COLUMN codedevbo TO code;

ALTER TABLE work_time_project."release"
    ADD project_id int8;
ALTER TABLE work_time_project."work"
    ADD project_id int8;
ALTER TABLE work_time_project."task"
    ADD project_id int8;
ALTER TABLE work_time_project."work_time"
    ADD project_id int8;

UPDATE work_time_project."release"
SET project_id = 1
WHERE true;

UPDATE work_time_project."work"
SET project_id = 1
WHERE true;

UPDATE work_time_project."task"
SET project_id = 1
WHERE true;

UPDATE work_time_project."work_time"
SET project_id = 1
WHERE true;

ALTER TABLE work_time_project."release"
    ALTER COLUMN "project_id" SET NOT NULL;
ALTER TABLE work_time_project."work"
    ALTER COLUMN "project_id" SET NOT NULL;
ALTER TABLE work_time_project."task"
    ALTER COLUMN "project_id" SET NOT NULL;
ALTER TABLE work_time_project."work_time"
    ALTER COLUMN "project_id" SET NOT NULL;



