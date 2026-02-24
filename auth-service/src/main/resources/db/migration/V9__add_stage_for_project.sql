-- Column: user_repo.project.stage_0_name

-- ALTER TABLE IF EXISTS user_repo.project DROP COLUMN IF EXISTS stage_0_name;

ALTER TABLE IF EXISTS user_repo.project
    ADD COLUMN stage_0_name character varying(255) COLLATE pg_catalog."default";

-- Column: user_repo.project.stage_1_name

-- ALTER TABLE IF EXISTS user_repo.project DROP COLUMN IF EXISTS stage_1_name;

ALTER TABLE IF EXISTS user_repo.project
    ADD COLUMN stage_1_name character varying(255) COLLATE pg_catalog."default";

-- Column: user_repo.project.stage_2_name

-- ALTER TABLE IF EXISTS user_repo.project DROP COLUMN IF EXISTS stage_2_name;

ALTER TABLE IF EXISTS user_repo.project
    ADD COLUMN stage_2_name character varying(255) COLLATE pg_catalog."default";

-- Column: user_repo.project.stage_3_name

-- ALTER TABLE IF EXISTS user_repo.project DROP COLUMN IF EXISTS stage_3_name;

ALTER TABLE IF EXISTS user_repo.project
    ADD COLUMN stage_3_name character varying(255) COLLATE pg_catalog."default";

-- Column: user_repo.project.stage_4_name

-- ALTER TABLE IF EXISTS user_repo.project DROP COLUMN IF EXISTS stage_4_name;

ALTER TABLE IF EXISTS user_repo.project
    ADD COLUMN stage_4_name character varying(255) COLLATE pg_catalog."default";

-- Column: user_repo.project.stage_end

-- ALTER TABLE IF EXISTS user_repo.project DROP COLUMN IF EXISTS stage_end;

ALTER TABLE IF EXISTS user_repo.project
    ADD COLUMN stage_end integer;


UPDATE user_repo.project
SET stage_end    = 5,
    stage_0_name = 'Анализ',
    stage_1_name = 'Разработка прототипа',
    stage_2_name = 'Стабилизация прототипа',
    stage_3_name = 'Стабилизация релиза',
    stage_4_name = 'ОПЭ'
WHERE true;
ALTER TABLE user_repo.project
    ALTER COLUMN "stage_end" SET NOT NULL;

ALTER TABLE user_repo.project
    ALTER COLUMN "code" SET NOT NULL;


