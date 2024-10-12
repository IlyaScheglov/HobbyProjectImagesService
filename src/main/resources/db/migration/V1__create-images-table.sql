create table images(
    id uuid primary key,
    title varchar(100) not null,
    photo_url text not null,
    user_id uuid not null
);