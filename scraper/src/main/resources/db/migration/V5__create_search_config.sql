CREATE TABLE search_config
(
    id            SERIAL NOT NULL,
    name          TEXT   NOT NULL,
    category      TEXT   NOT NULL,
    art           TEXT   NOT NULL,
    plz           TEXT   NOT NULL,
    search_term   TEXT   NOT NULL,
    radius        INTEGER,
    PRIMARY KEY (id),
        CONSTRAINT search_config_unique
        UNIQUE (name, category, art, plz, search_term, radius)
);

-- one time migration, comment out
INSERT INTO search_config (name, category, art, plz, search_term, radius)
VALUES ('rennraeder-berlin-lichtenrade',
        'fahrraeder',
        'herren',
        '12309',
        'rennrad',
        10)
RETURNING id;


ALTER TABLE listing
    ADD COLUMN search_config_id INT REFERENCES search_config (id);


ALTER TABLE scrape
    ADD COLUMN search_config_id INT REFERENCES search_config (id);


-- one time migration, comment out
UPDATE listing
SET search_config_id = 1;

UPDATE scrape
SET search_config_id = 1;



ALTER TABLE listing
    ALTER COLUMN search_config_id SET NOT NULL;

ALTER TABLE scrape
    ALTER COLUMN search_config_id SET NOT NULL;

