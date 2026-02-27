ALTER TABLE user_repo.user_info_type
    ADD project_id int8;
DROP INDEX user_info_type_code_idx;
CREATE UNIQUE INDEX user_info_type_code_idx ON user_repo.user_info_type USING btree (code, user_id, project_id);

