create table nace
(
    `order` int auto_increment
        primary key,
    level int null,
    code varchar(64) null,
    parent varchar(64) null,
    description text null,
    includes text null,
    also_includes text null,
    rulings text null,
    excludes text null,
    reference varchar(64) null
);