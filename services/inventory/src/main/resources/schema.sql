create schema if not exists bazaar AUTHORIZATION postgres;

drop table if exists bazaar.product_categories cascade;
create table bazaar.product_categories(
	id serial primary key,
	name varchar(500)
);


drop table if exists bazaar.products;
create table bazaar.products(
	id serial primary key,
	name varchar(500),
	category_id integer,
	price float,
	quantity integer,
	updated_at timestamp,
	constraint fk_category_id foreign key (category_id) references bazaar.product_categories(id)
);

INSERT INTO bazaar.product_categories (name)
VALUES
('Electronics'),
('Clothing'),
('Books'),
('Home Appliances'),
('Sports'),
('Toys'),
('Groceries'),
('Furniture'),
('Health & Beauty'),
('Automotive');

INSERT INTO bazaar.products (name, category_id, price, quantity, updated_at)
VALUES
('Smartphone', 1, 699.99, 50, CURRENT_TIMESTAMP),
('Laptop', 1, 999.99, 30, CURRENT_TIMESTAMP),
('T-Shirt', 2, 19.99, 100, CURRENT_TIMESTAMP),
('Novel', 3, 14.99, 200, CURRENT_TIMESTAMP),
('Microwave', 4, 89.99, 40, CURRENT_TIMESTAMP),
('Basketball', 5, 25.99, 60, CURRENT_TIMESTAMP),
('Action Figure', 6, 12.99, 150, CURRENT_TIMESTAMP),
('Organic Apples', 7, 3.99, 300, CURRENT_TIMESTAMP),
('Sofa', 8, 499.99, 10, CURRENT_TIMESTAMP),
('Shampoo', 9, 7.99, 80, CURRENT_TIMESTAMP);

select * from bazaar.products;
