CREATE TABLE notifications (
                               id SERIAL PRIMARY KEY,
                               recipient VARCHAR(50) NOT NULL,
                               subject VARCHAR(100) NOT NULL,
                               body TEXT NOT NULL,
                               sent_at TIMESTAMP NOT NULL,
                               status VARCHAR(50) NOT NULL
);