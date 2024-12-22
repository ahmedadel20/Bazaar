CREATE SCHEMA IF NOT EXISTS bazaar
    AUTHORIZATION postgres;

drop table if exists bazaar.customer cascade;
create table bazaar.customer(
	id serial primary key,
	firstName varchar(50),
	lastName varchar(50),
	email varchar(50)
);

drop table if exists bazaar.address cascade;
create table bazaar.address(
	customerId serial primary key,
	street varchar(50),
	city varchar(50),
	zipCode int,
	constraint fk_customerAddress foreign key (customerId) references bazaar.customer(id)
);


drop table if exists bazaar.cart_item cascade;
create table bazaar.cart_item(
	id serial primary key,
	customerId int,
	productId int,
	quantity int,
	constraint fk_cartItems foreign key (customerId) references bazaar.customer(id)
);


