ALTER TABLE work_time_project.work_time
    ALTER COLUMN work_date TYPE date USING work_date::date;
ALTER TABLE work_time_project.task
    ALTER COLUMN time_create TYPE timestamp(6) USING time_create::timestamp;
ALTER TABLE work_time_project.task
    ALTER COLUMN "refresh" TYPE timestamp(6) USING "refresh"::timestamp;
