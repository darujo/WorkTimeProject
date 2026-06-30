ALTER TABLE user_repo.vacation
    ALTER COLUMN date_start TYPE date USING date_start::date;
ALTER TABLE user_repo.vacation
    ALTER COLUMN date_end TYPE date USING date_end::date;
