create schema if not exists bazaar AUTHORIZATION postgres;

drop table if exists bazaar.product_categories cascade;
create table bazaar.product_categories(
	id serial primary key,
	name varchar(500) unique
);


drop table if exists bazaar.products cascade;
create table bazaar.products(
	id serial primary key,
	name varchar(500),
	category_id integer,
	original_price float,
	current_price float,
	quantity integer,
	updated_at timestamp,
	constraint fk_category_id foreign key (category_id) references bazaar.product_categories(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS bazaar.cart_items CASCADE;
CREATE TABLE bazaar.cart_items (
    id SERIAL PRIMARY KEY,
    bazaar_user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    constraint fk_product_id foreign key (product_id) references bazaar.products(id) ON DELETE CASCADE
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

INSERT INTO bazaar.products (name, category_id, original_price, current_price, quantity, updated_at)
VALUES
('Smartphone', 1, 699.99, 699.99, 50, CURRENT_TIMESTAMP),
('Laptop', 1, 999.99, 999.99, 30, CURRENT_TIMESTAMP),
('T-Shirt', 2, 19.99, 19.99, 100, CURRENT_TIMESTAMP),
('Novel', 3, 14.99, 14.99, 200, CURRENT_TIMESTAMP),
('Microwave', 4, 89.99, 89.99, 40, CURRENT_TIMESTAMP),
('Basketball', 5, 25.99, 25.99, 60, CURRENT_TIMESTAMP),
('Action Figure', 6, 12.99, 12.99, 150, CURRENT_TIMESTAMP),
('Organic Apples', 7, 3.99, 3.99, 300, CURRENT_TIMESTAMP),
('Sofa', 8, 499.99, 499.99, 10, CURRENT_TIMESTAMP),
('Shampoo', 9, 7.99, 7.99, 80, CURRENT_TIMESTAMP);


INSERT INTO bazaar.cart_items (bazaar_user_id, product_id, quantity)
VALUES
    (1, 1, 2), -- 2 Smartphones
    (2, 3, 5),  -- 5 T-Shirts
    (3, 4, 1),  -- 1 Novel
    (4, 5, 3),  -- 3 Microwaves
    (5, 8, 10);  -- 10 Organic Apples
