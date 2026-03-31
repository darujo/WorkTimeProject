ALTER TABLE information.message_information
    ADD COLUMN "file_for_disk" character varying(255) COLLATE pg_catalog."default";
ALTER TABLE information.message_information
    ADD COLUMN project_id int8;
                    
