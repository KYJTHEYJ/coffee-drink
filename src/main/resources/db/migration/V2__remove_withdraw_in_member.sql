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