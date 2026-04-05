ALTER TABLE menu_order_count
DROP
COLUMN count_dt;

ALTER TABLE menu_order_count
    ADD count_dt date NOT NULL;