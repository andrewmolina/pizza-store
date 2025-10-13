-- Create Pizza table
CREATE TABLE IF NOT EXISTS pizza (
    item_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,
    price NUMERIC(10, 2) NOT NULL
);

-- Create Customer table
CREATE TABLE IF NOT EXISTS customer (
    customer_id SERIAL PRIMARY KEY,
    firstname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100)
);

-- Create Order table (quoted because "order" is a reserved keyword in PostgreSQL)
CREATE TABLE IF NOT EXISTS "order" (
    order_id SERIAL PRIMARY KEY,
    item_id BIGINT NOT NULL,
    subtotal NUMERIC(10, 2) NOT NULL,
    tax NUMERIC(10, 2) NOT NULL,
    total NUMERIC(10, 2) NOT NULL,
    customer_id BIGINT NOT NULL
);

-- Insert sample pizzas
INSERT INTO pizza (name, description, price) VALUES
    ('Margherita', 'Fresh mozzarella, tomato sauce, and basil', 12.99),
    ('Pepperoni', 'Classic pepperoni with mozzarella cheese', 14.99),
    ('Hawaiian', 'Ham, pineapple, and mozzarella', 15.99),
    ('BBQ Chicken', 'BBQ sauce, grilled chicken, red onions, and cilantro', 16.99),
    ('Veggie Supreme', 'Mushrooms, bell peppers, onions, olives, and tomatoes', 15.49),
    ('Meat Lovers', 'Pepperoni, sausage, bacon, and ham', 17.99),
    ('Four Cheese', 'Mozzarella, parmesan, gorgonzola, and ricotta', 16.49),
    ('Buffalo Chicken', 'Spicy buffalo sauce, chicken, and ranch drizzle', 16.99);

-- Insert sample customers
INSERT INTO customer (firstname, lastname, phone, email) VALUES
    ('John', 'Doe', '555-0101', 'john.doe@email.com'),
    ('Jane', 'Smith', '555-0102', 'jane.smith@email.com'),
    ('Mike', 'Johnson', '555-0103', 'mike.johnson@email.com'),
    ('Sarah', 'Williams', '555-0104', 'sarah.williams@email.com'),
    ('David', 'Brown', '555-0105', 'david.brown@email.com');

-- Insert sample orders
INSERT INTO "order" (item_id, subtotal, tax, total, customer_id) VALUES
    (1, 12.99, 1.04, 14.03, 1),
    (2, 14.99, 1.20, 16.19, 2),
    (3, 15.99, 1.28, 17.27, 1),
    (6, 17.99, 1.44, 19.43, 3),
    (5, 15.49, 1.24, 16.73, 4);

-- Display counts
SELECT 'Pizzas created: ' || COUNT(*) FROM pizza;
SELECT 'Customers created: ' || COUNT(*) FROM customer;
SELECT 'Orders created: ' || COUNT(*) FROM "order";

