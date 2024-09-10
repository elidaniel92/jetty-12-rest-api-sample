CREATE DATABASE IF NOT EXISTS store;
USE store;

CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    buy_price SMALLINT NOT NULL,
    quantity_in_stock SMALLINT NOT NULL
);

INSERT INTO products (name, buy_price, quantity_in_stock) VALUES 
('Laptop', 1000, 50),
('Smartphone', 700, 120),
('Headphones', 100, 200),
('Monitor', 300, 75),
('Keyboard', 50, 150);
