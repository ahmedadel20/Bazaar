create schema if not exists bazaar AUTHORIZATION postgres;

DROP TABLE IF EXISTS bazaar.orders CASCADE;
CREATE TABLE bazaar.orders (
    id SERIAL PRIMARY KEY,
    bazaar_user_id BIGINT NOT NULL,
    description VARCHAR(500) NOT NULL,
    final_price NUMERIC(10, 2) NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS bazaar.transactions CASCADE;
CREATE TABLE bazaar.transactions (
    id SERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    payment_status VARCHAR(50) NOT NULL,
    final_price NUMERIC(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES bazaar.orders(id) ON DELETE CASCADE
);


INSERT INTO bazaar.orders (bazaar_user_id, description, final_price, order_date)
VALUES
    (1, 'Electronics Order', 1399.98, CURRENT_TIMESTAMP), -- Order for 2 Smartphones
    (2, 'Clothing Order', 99.95, CURRENT_TIMESTAMP),     -- Order for 5 T-Shirts
    (3, 'Book Purchase', 14.99, CURRENT_TIMESTAMP),      -- Order for 1 Novel
    (4, 'Appliance Order', 269.97, CURRENT_TIMESTAMP),   -- Order for 3 Microwaves
    (5, 'Groceries Purchase', 39.90, CURRENT_TIMESTAMP); -- Order for 10 Apples

INSERT INTO bazaar.transactions (order_id, payment_status, final_price, created_at)
VALUES
    (1, 'Completed', 1399.98, CURRENT_TIMESTAMP), -- Transaction for first order
    (2, 'Completed', 99.95, CURRENT_TIMESTAMP),   -- Transaction for second order
    (3, 'Pending', 14.99, CURRENT_TIMESTAMP),     -- Transaction for third order
    (4, 'Completed', 269.97, CURRENT_TIMESTAMP),  -- Transaction for fourth order
    (5, 'Completed', 39.90, CURRENT_TIMESTAMP);   -- Transaction for fifth order
