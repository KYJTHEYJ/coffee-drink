DROP TABLE order_menu_options;

ALTER TABLE orders
DROP
COLUMN status;

ALTER TABLE orders
DROP
COLUMN total_price;

ALTER TABLE order_menus
DROP
COLUMN price_snap;

ALTER TABLE order_menus
DROP
COLUMN quantity;

ALTER TABLE order_menus
DROP
COLUMN total_price;

ALTER TABLE order_menus
    ADD price_snap DECIMAL NOT NULL;

ALTER TABLE menu_stocks
DROP
COLUMN quantity;

ALTER TABLE menu_stocks
    ADD quantity DECIMAL NOT NULL;

ALTER TABLE order_menus
    ADD quantity DECIMAL NOT NULL;

ALTER TABLE order_menus
    ADD total_price DECIMAL NOT NULL;

ALTER TABLE orders
    ADD total_price DECIMAL NOT NULL;