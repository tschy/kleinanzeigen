CREATE TABLE listing (
      id                BIGINT   NOT NULL,
      title             TEXT     NOT NULL,
      price             MONEY,
      negotiable        BOOLEAN  NOT NULL,
      created           DATE   NOT NULL ,
      first_scrape      TIMESTAMP NOT NULL,
      last_scrape       TIMESTAMP  NOT NULL,
      scrape_count      INTEGER  NOT NULL,
    PRIMARY KEY (id, first_scrape)
  );