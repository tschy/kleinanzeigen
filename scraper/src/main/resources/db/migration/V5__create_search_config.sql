CREATE TABLE search_config
(
    id            SERIAL NOT NULL,
    category      TEXT   NOT NULL,
    art           TEXT   NOT NULL,
    plz           TEXT   NOT NULL,
    search_term   TEXT   NOT NULL,
    radius        INTEGER,
    parent_config INT REFERENCES search_config (id),
    PRIMARY KEY (id),
        CONSTRAINT search_config_unique
        UNIQUE (category, art, plz, search_term, radius)
);

-- LEGACY, comment out
INSERT INTO search_config (category, art, plz, search_term, radius)
VALUES ('fahrraeder', 'herren', '12309', 'rennrad', 10)
RETURNING id;


ALTER TABLE listing
    ADD COLUMN search_config_id INT REFERENCES search_config (id);

-- LEGACY, comment out
UPDATE listing
SET search_config_id = 1;

-- LEGACY, comment out and add do first alter table statement
ALTER TABLE listing
    ALTER COLUMN search_config_id SET NOT NULL;