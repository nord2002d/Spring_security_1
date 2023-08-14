create table users
(
    id        bigint auto_increment
        primary key,
    age       int          null,
    user_name varchar(255) null,
    password  varchar(255) null,
    sur_name  varchar(255) null
);

create table user_role
(
    user_id bigint       not null,
    roles   varchar(255) null,
    constraint FKj345gk1bovqvfame88rcx7yyx
        foreign key (user_id) references users (id)
);

