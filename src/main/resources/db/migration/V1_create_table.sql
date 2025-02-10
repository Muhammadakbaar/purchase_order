CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(500),
    last_name VARCHAR(500),
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50),
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    created_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE items (
    id SERIAL PRIMARY KEY,
    name VARCHAR(500) NOT NULL,
    description VARCHAR(500),
    price INT NOT NULL,
    cost INT NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    created_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE po_h (
    id SERIAL PRIMARY KEY,
    datetime TIMESTAMP NOT NULL,
    description VARCHAR(500),
    total_price INT NOT NULL,
    total_cost INT NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    created_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE po_d (
    id SERIAL PRIMARY KEY,
    poh_id INT REFERENCES po_h(id) ON DELETE CASCADE,
    item_id INT REFERENCES items(id) ON DELETE CASCADE,
    item_qty INT NOT NULL,
    item_cost INT NOT NULL,
    item_price INT NOT NULL
);
