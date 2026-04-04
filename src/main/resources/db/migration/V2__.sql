ALTER TABLE members
    ADD CONSTRAINT uk_members_id UNIQUE (id);

ALTER TABLE menu_categories
    ADD CONSTRAINT uk_menu_categories_id UNIQUE (id);

ALTER TABLE menu_option_categories
    ADD CONSTRAINT uk_menu_option_categories_id UNIQUE (id);

ALTER TABLE menu_options
    ADD CONSTRAINT uk_menu_options_id UNIQUE (id);

ALTER TABLE menu_order_count
    ADD CONSTRAINT uk_menu_order_count_id UNIQUE (id);

ALTER TABLE menu_stocks
    ADD CONSTRAINT uk_menu_stocks_id UNIQUE (id);

ALTER TABLE menus
    ADD CONSTRAINT uk_menus_id UNIQUE (id);

ALTER TABLE order_menu_options
    ADD CONSTRAINT uk_order_menu_options_id UNIQUE (id);

ALTER TABLE order_menus
    ADD CONSTRAINT uk_order_menus_id UNIQUE (id);

ALTER TABLE orders
    ADD CONSTRAINT uk_orders_id UNIQUE (id);

ALTER TABLE point
    ADD CONSTRAINT uk_point_id UNIQUE (id);

ALTER TABLE point_logs
    ADD CONSTRAINT uk_point_logs_id UNIQUE (id);

ALTER TABLE members
DROP
COLUMN withdraw;

ALTER TABLE members
DROP
COLUMN withdraw_at;

ALTER TABLE members
DROP
COLUMN `role`;

ALTER TABLE members
    ADD `role` VARCHAR(30) NOT NULL;

ALTER TABLE orders
DROP
COLUMN status;

ALTER TABLE orders
    ADD status VARCHAR(30) NOT NULL;

ALTER TABLE point_logs
DROP
COLUMN type;

ALTER TABLE point_logs
    ADD type VARCHAR(255) NOT NULL;