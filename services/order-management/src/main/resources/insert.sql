INSERT INTO bazaar.cart_item (bazaar_user_id, product_id, quantity, current_price)
VALUES
    (101, 1001, 2, 29.99),
    (102, 1002, 1, 15.50),
    (103, 1003, 3, 9.75),
    (101, 1004, 1, 50.00);

INSERT INTO bazaar.order (bazaar_user_id, description)
VALUES
    (101, 'Order for electronics and home goods'),
    (102, 'Order for kitchen appliances'),
    (103, 'Order for books and office supplies');

INSERT INTO bazaar.transaction (order_id, payment_status, final_price, created_at)
VALUES
    (1, 'PAID', 110.00, '2024-12-25'),
    (1, 'PENDING', 110.00, '2024-12-25'),
    (2, 'PAID', 65.00, '2024-12-24'),
    (3, 'FAILED', 75.00, '2024-12-23');