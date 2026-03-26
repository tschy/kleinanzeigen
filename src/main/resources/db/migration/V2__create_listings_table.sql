CREATE TABLE listings (
      id         BIGINT   NOT NULL,
      title      TEXT     NOT NULL,
      price      MONEY,
      negotiable BOOLEAN  NOT NULL,
      created    DATE   NOT NULL ,
      PRIMARY KEY (id)
  );