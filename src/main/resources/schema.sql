create table if not exists product
(
    id            bigserial primary key,
    sku           varchar(30) unique not null,
    name          varchar(400)       not null,
    price         decimal(10, 2)     not null,
    creation_date date               not null,
    deleted       boolean            default (false)
);

create index if not exists idx_product_sku on product (sku);

create table if not exists customer_order
(
    id          bigserial primary key,
    buyer_email varchar(254),
    placed_time timestamp with time zone
);

create table if not exists order_product
(
    id         bigserial primary key,
    order_id   bigint references customer_order,
    product_id bigint references product
);
