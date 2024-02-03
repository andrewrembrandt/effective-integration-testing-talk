insert into product(id, sku, name, price, creation_date, deleted)
values (1000, 'A1213', 'Watch', 30.50, date '2001-01-01', false),
       (1001, 'A1214', 'Tablet', 700.90, date '2001-01-02', false)
on conflict do nothing;

insert into customer_order(id, buyer_email, placed_time)
values (2000, 'me@me.com', '2011-01-01 20:00:00'::TIMESTAMP WITH TIME ZONE),
       (2001, 'myself@me.com', '2015-01-01 10:00:00'::TIMESTAMP WITH TIME ZONE)
on conflict do nothing;

insert into order_product(id, order_id, product_id)
values (3000, 2000, 1000),
       (3001, 2000, 1000),
       (3002, 2001, 1001),
       (3003, 2001, 1001)
on conflict do nothing;
