create table users
(
    id         bigint not null auto_increment,
    birthday   date,
    email      varchar(255) not null,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    phone      varchar(255) not null,
    primary key (id)
);

alter table users
    add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

alter table users
    add constraint UKdu5v5sr43g5bfnji4vb8hg5s3 unique (phone);