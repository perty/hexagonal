create table account
(
    id bigint auto_increment primary key
);

create table activity
(
    id                bigint auto_increment primary key,
    timestamp         timestamp,
    owner_account_id  bigint,
    source_account_id bigint,
    target_account_id bigint,
    amount            bigint
);