CREATE DATABASE IF NOT EXISTS Inventory;
USE Inventory;

DROP TABLE IF EXISTS Stock;

CREATE TABLE Stock(
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(255),
    location VARCHAR(255)
);

INSERT INTO Stock(name, location) VALUES('Shopify T-Shirt', 'Las Vegas');
INSERT INTO Stock(name, location) VALUES('Corsair RMx 750W Power Supply', 'London');
INSERT INTO Stock(name, location) VALUES('Nord Stage 3 88-Key', 'Singapore');
