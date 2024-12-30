DROP TABLE IF EXISTS bazaar.cart_item CASCADE;
CREATE TABLE bazaar.cart_item (
                                  id SERIAL PRIMARY KEY,
                                  bazaar_user_id BIGINT NOT NULL,
                                  product_id BIGINT NOT NULL,
                                  quantity INT NOT NULL,
                                  current_price NUMERIC(10, 2) NOT NULL
);

DROP TABLE IF EXISTS bazaar.order CASCADE;
CREATE TABLE bazaar.order (
                              id SERIAL PRIMARY KEY,
                              bazaar_user_id BIGINT NOT NULL,
                              description VARCHAR(500) NOT NULL,
                              final_price NUMERIC(10, 2) NOT NULL,
                              order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS bazaar.transaction CASCADE;
CREATE TABLE bazaar.transaction (
                                    id SERIAL PRIMARY KEY,
                                    order_id BIGINT NOT NULL,
                                    payment_status VARCHAR(50) NOT NULL,
                                    final_price NUMERIC(10, 2) NOT NULL,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES bazaar.order(id) ON DELETE CASCADE
);


INSERT INTO bazaar.cart_item (bazaar_user_id, product_id, quantity, current_price)
VALUES
    (1, 1, 2, 699.99), -- 2 Smartphones
    (2, 3, 5, 19.99),  -- 5 T-Shirts
    (3, 4, 1, 14.99),  -- 1 Novel
    (4, 5, 3, 89.99),  -- 3 Microwaves
    (5, 8, 10, 3.99);  -- 10 Organic Apples


INSERT INTO bazaar.order (bazaar_user_id, description, final_price, order_date)
VALUES
    (1, 'Electronics Order', 1399.98, CURRENT_TIMESTAMP), -- Order for 2 Smartphones
    (2, 'Clothing Order', 99.95, CURRENT_TIMESTAMP),     -- Order for 5 T-Shirts
    (3, 'Book Purchase', 14.99, CURRENT_TIMESTAMP),      -- Order for 1 Novel
    (4, 'Appliance Order', 269.97, CURRENT_TIMESTAMP),   -- Order for 3 Microwaves
    (5, 'Groceries Purchase', 39.90, CURRENT_TIMESTAMP); -- Order for 10 Apples

INSERT INTO bazaar.transaction (order_id, payment_status, final_price, created_at)
VALUES
    (1, 'Completed', 1399.98, CURRENT_TIMESTAMP), -- Transaction for first order
    (2, 'Completed', 99.95, CURRENT_TIMESTAMP),   -- Transaction for second order
    (3, 'Pending', 14.99, CURRENT_TIMESTAMP),     -- Transaction for third order
    (4, 'Completed', 269.97, CURRENT_TIMESTAMP),  -- Transaction for fourth order
    (5, 'Completed', 39.90, CURRENT_TIMESTAMP);   -- Transaction for fifth order
