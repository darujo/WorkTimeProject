-- Database: work_time_monitor

-- DROP DATABASE IF EXISTS work_time_monitor;

--CREATE DATABASE work_time_monitor
--    WITH
--    OWNER = postgres
--    ENCODING = 'UTF8'
--    LC_COLLATE = 'Russian_Russia.1251'
--    LC_CTYPE = 'Russian_Russia.1251'
--    LOCALE_PROVIDER = 'libc'
--    TABLESPACE = pg_default
--    CONNECTION LIMIT = -1
--   IS_TEMPLATE = False;


-- SCHEMA: monitor

-- DROP SCHEMA IF EXISTS monitor ;

CREATE SCHEMA IF NOT EXISTS monitor
    AUTHORIZATION postgres;
