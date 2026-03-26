DROP TABLE listings;

create table LISTINGS (
    ID BIGINT not null,
    TITLE TEXT not null,
    PRICE NUMERIC not null,
    NEGOTIABLE BOOLEAN not null,
    CREATED DATE
);