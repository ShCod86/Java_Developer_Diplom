CREATE SCHEMA IF NOT EXISTS dbo;
create table if not exists dbo.users
(
    id serial primary key,
    login varchar(50) not null unique,
    password varchar(100) not null
);


create table  if not exists dbo.files
(
    id serial primary key,
    file_name varchar(50),
    type varchar(10) not null ,
    content bytea not null,
    create_date timestamp,
    size bigint not null,
    user_id serial,
    CONSTRAINT fk_users_files FOREIGN KEY (user_id)
        REFERENCES dbo.users (id) MATCH SIMPLE
);