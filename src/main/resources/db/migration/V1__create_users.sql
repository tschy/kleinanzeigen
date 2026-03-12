-- =============================================================
-- Flyway Migration: V1__create_users.sql
-- Description: Create initial database users: crawler, analyst
-- =============================================================

-- -------------------------------------------------------------
-- Create the 'crawler' user
-- Role: intended for automated data ingestion / web crawling
-- Permissions: connect + write to relevant schemas as needed
-- -------------------------------------------------------------
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'crawler') THEN
CREATE ROLE crawler
    WITH LOGIN
    PASSWORD 'CHANGE_ME_crawler'   -- Replace with a strong password or use a secrets manager
    NOSUPERUSER
    NOCREATEDB
    NOCREATEROLE
    NOINHERIT
    CONNECTION LIMIT 10;

COMMENT ON ROLE crawler IS 'Service account for automated crawling / data ingestion pipelines';
END IF;
END
$$;

-- -------------------------------------------------------------
-- Create the 'analyst' user
-- Role: intended for read-only / analytical queries
-- Permissions: connect + read access to relevant schemas
-- -------------------------------------------------------------
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'analyst') THEN
CREATE ROLE analyst
    WITH LOGIN
    PASSWORD 'CHANGE_ME_analyst'   -- Replace with a strong password or use a secrets manager
    NOSUPERUSER
    NOCREATEDB
    NOCREATEROLE
    NOINHERIT
    CONNECTION LIMIT 20;

COMMENT ON ROLE analyst IS 'Service account for read-only analytical access';
END IF;
END
$$;

-- =============================================================
-- Grant baseline privileges
-- Adjust schema names below to match your actual schema layout
-- =============================================================

-- Allow both users to connect to this database
GRANT CONNECT ON DATABASE CURRENT_DATABASE() TO crawler;
GRANT CONNECT ON DATABASE CURRENT_DATABASE() TO analyst;

-- Allow usage of the public schema (replace 'public' as needed)
GRANT USAGE ON SCHEMA public TO crawler;
GRANT USAGE ON SCHEMA public TO analyst;

-- crawler: read + write on all current and future tables in public
GRANT SELECT, INSERT, UPDATE, DELETE
      ON ALL TABLES IN SCHEMA public TO crawler;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO crawler;

-- analyst: read-only on all current and future tables in public
GRANT SELECT
      ON ALL TABLES IN SCHEMA public TO analyst;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT SELECT ON TABLES TO analyst;

-- analyst: read access to sequences (needed for some BI tools)
GRANT SELECT
      ON ALL SEQUENCES IN SCHEMA public TO analyst;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT SELECT ON SEQUENCES TO analyst;