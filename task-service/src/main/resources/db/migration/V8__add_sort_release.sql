ALTER TABLE work_time_project."release"
    ADD sort real NULL;
CREATE INDEX release_sort_idx ON work_time_project."release" (sort, "name");

