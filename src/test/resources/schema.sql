CREATE SCHEMA papel_orders;

CREATE TABLE papel_orders.orders (
	order_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
	order_number VARCHAR(50) UNIQUE NOT NULL,
	order_status VARCHAR(20) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL,
	version INT NOT NULL
);

CREATE TABLE papel_orders.order_items (
	order_item_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
	product_name VARCHAR(100) NOT NULL,
	quantity INTEGER NOT NULL,
	unit_price NUMERIC(6, 2) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL,
	order_id INTEGER REFERENCES papel_orders.orders(order_id) ON DELETE CASCADE
);