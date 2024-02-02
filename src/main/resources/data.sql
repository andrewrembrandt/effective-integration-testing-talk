insert into tenant(id, name)
values (1, 'andrew'),
       (2, 'foo'),
       (3, 'bar')
on conflict do nothing;

insert into product(id, sku, name, price, creation_date, deleted, tenant_id)
values (1000, 'A1213', 'Watch', 30.50, date '2001-01-01', false, 1),
       (1001, 'A1214', 'Tablet', 700.90, date '2001-01-02', false, 2)
on conflict do nothing;

insert into customer_order(id, buyer_email, placed_time, tenant_id)
values (2000, 'me@me.com', '2011-01-01 20:00:00'::TIMESTAMP WITH TIME ZONE, 1),
       (2001, 'myself@me.com', '2015-01-01 10:00:00'::TIMESTAMP WITH TIME ZONE, 2)
on conflict do nothing;

insert into order_product(id, order_id, product_id, tenant_id)
values (3000, 2000, 1000, 1),
       (3001, 2000, 1000, 1),
       (3002, 2001, 1001, 2),
       (3003, 2001, 1001, 2)
on conflict do nothing;
