CREATE TABLE listing (
      id                TEXT   NOT NULL,
      first_scrape      TIMESTAMP NOT NULL,
      last_scrape       TIMESTAMP  NOT NULL,
      scrape_count      INTEGER  NOT NULL,
      title             TEXT     NOT NULL,
      price             NUMERIC(10, 2),
      negotiable        BOOLEAN  NOT NULL,
      created           DATE,


    PRIMARY KEY (id, first_scrape)
  );