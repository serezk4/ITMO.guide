\c lab7

DROP TABLE IF EXISTS users cascade;
DROP TABLE IF EXISTS persons cascade;

create table users
(
    id       serial primary key,
    username text not null unique,
    password text not null
);


create table persons
(
    id            serial primary key,
    owner_id      bigint not null references users (id),
    name          text   not null,
    cord_x        int    not null,
    cord_y        int    not null,
    creation_date timestamp default now(),
    height        int    not null,
    weight        int    not null,
    color         text   not null,
    country       text   not null,
    location_x    float  not null,
    location_y    float  null,
    location_name text   null
)