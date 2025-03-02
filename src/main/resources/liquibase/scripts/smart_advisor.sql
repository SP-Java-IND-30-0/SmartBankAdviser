-- liquibase formatted sql

-- changeset belousoveu:1
create table if not exists public.dynamic_rule
(
    product_id   uuid not null
        primary key,
    product_name varchar(255),
    product_text varchar(255)
);

-- changeset belousoveu:2
create table if not exists public.simple_rules
(
    id           integer generated by default as identity
        primary key,
    negate       boolean not null,
    query_type   smallint
        constraint simple_rules_query_type_check
            check ((query_type >= 0) AND (query_type <= 3)),
    arguments_id integer
        constraint uk_simple_rules_arguments_id
            unique
);

create table public.rule_arguments
(
    query_type     varchar(31) not null,
    id             integer generated by default as identity
        primary key,
    simple_rule_id integer
        constraint uk_rule_arguments_simple_rule_id
            unique
);

-- changeset belousoveu:3
alter table public.simple_rules
    add constraint fk_simple_rules_arguments_id
        foreign key (arguments_id) references public.rule_arguments (id);

alter table public.rule_arguments
    add constraint fk_rule_arguments_simple_rule_id
        foreign key (simple_rule_id) references public.simple_rules (id);

-- changeset belousoveu:4
create table public.rule_user_of
(
    product_type smallint
        constraint rule_user_of_product_type_check
            check ((product_type >= 0) AND (product_type <= 3)),
    id           integer not null
        primary key
        constraint fk_rule_user_of_id
            references public.rule_arguments
);

create table public.rule_active_user_of
(
    product_type smallint
        constraint rule_active_user_of_product_type_check
            check ((product_type >= 0) AND (product_type <= 3)),
    id           integer not null
        primary key
        constraint fk_rule_active_user_of_id
            references public.rule_arguments
);

create table public.rule_compare_sum
(
    amount         integer not null,
    compare_type   smallint
        constraint rule_compare_sum_compare_type_check
            check ((compare_type >= 0) AND (compare_type <= 4)),
    operation_type smallint
        constraint rule_compare_sum_operation_type_check
            check ((operation_type >= 0) AND (operation_type <= 1)),
    product_type   smallint
        constraint rule_compare_sum_product_type_check
            check ((product_type >= 0) AND (product_type <= 3)),
    id             integer not null
        primary key
        constraint fk_rule_compare_sum_id
            references public.rule_arguments
);

create table public.rule_compare_operation_sum
(
    compare_type smallint
        constraint rule_compare_operation_sum_compare_type_check
            check ((compare_type >= 0) AND (compare_type <= 4)),
    product_type smallint
        constraint rule_compare_operation_sum_product_type_check
            check ((product_type >= 0) AND (product_type <= 3)),
    id           integer not null
        primary key
        constraint fk_rule_compare_operation_sum_id
            references public.rule_arguments
);

-- changeset belousoveu:5
create table public.dynamic_rules_simple_rules
(
    product_id uuid    not null
        constraint fk_dynamic_rules_simple_rules_product_id
            references public.dynamic_rule,
    rule_id    integer not null
        constraint fk_dynamic_rules_simple_rules_rule_id
            references public.simple_rules,
    constraint pk_dynamic_rules_simple_rules
        primary key (product_id, rule_id)
);


-- changeset belousoveu:6
alter table public.rule_compare_sum
    add constraint unique_rule_compare_sum_attributes
        unique (product_type, amount, compare_type, operation_type);

alter table public.rule_compare_operation_sum
    add constraint unique_rule_compare_operation_sum_attributes
        unique (product_type, compare_type);

alter table public.rule_user_of
    add constraint unique_rule_user_of_attributes
        unique (product_type);

alter table public.rule_active_user_of
    add constraint unique_rule_active_user_of_attributes
        unique (product_type);

-- changeset belousoveu:7
alter table public.rule_arguments
    alter column query_type type varchar(50) using query_type::varchar(50);

alter table rule_arguments drop column if exists simple_rule_id;


-- changeset belousoveu:8
alter table simple_rules drop constraint  fk_simple_rules_arguments_id;

alter table simple_rules drop constraint uk_simple_rules_arguments_id;

alter table simple_rules add constraint fk_simple_rules_arguments_id
    foreign key (arguments_id) references rule_arguments (id);
