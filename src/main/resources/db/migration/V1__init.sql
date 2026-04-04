CREATE TABLE members
(
    id          BINARY(16)   NOT NULL,
    created_at  datetime     NOT NULL,
    update_at   datetime     NOT NULL,
    email       VARCHAR(120) NOT NULL,
    name        VARCHAR(60)  NOT NULL,
    pwd         VARCHAR(300) NOT NULL,
    `role`      VARCHAR(30)  NOT NULL,
    withdraw    BIT(1)       NOT NULL,
    withdraw_at datetime NULL,
    CONSTRAINT pk_members PRIMARY KEY (id)
);

CREATE TABLE menu_categories
(
    id          BINARY(16)   NOT NULL,
    deleted     BIT(1)       NOT NULL,
    deleted_at  datetime NULL,
    created_at  datetime     NOT NULL,
    update_at   datetime     NOT NULL,
    name        VARCHAR(100) NOT NULL,
    sort_number INT          NOT NULL,
    CONSTRAINT pk_menu_categories PRIMARY KEY (id)
);

CREATE TABLE menu_option_categories
(
    id          BINARY(16)   NOT NULL,
    deleted     BIT(1)       NOT NULL,
    deleted_at  datetime NULL,
    created_at  datetime     NOT NULL,
    update_at   datetime     NOT NULL,
    menu_id     BINARY(16)   NOT NULL,
    name        VARCHAR(100) NOT NULL,
    sort_number INT          NOT NULL,
    essential   BIT(1)       NOT NULL,
    CONSTRAINT pk_menu_option_categories PRIMARY KEY (id)
);

CREATE TABLE menu_options
(
    id                      BINARY(16)   NOT NULL,
    deleted                 BIT(1)       NOT NULL,
    deleted_at              datetime NULL,
    created_at              datetime     NOT NULL,
    update_at               datetime     NOT NULL,
    menu_option_category_id BINARY(16)   NOT NULL,
    name                    VARCHAR(100) NOT NULL,
    price                   BIGINT       NOT NULL,
    sort_number             INT          NOT NULL,
    is_default              BIT(1)       NOT NULL,
    CONSTRAINT pk_menu_options PRIMARY KEY (id)
);

CREATE TABLE menu_order_count
(
    id          BINARY(16) NOT NULL,
    menu_id     BINARY(16) NOT NULL,
    order_count BIGINT   NOT NULL,
    count_dt    datetime NOT NULL,
    CONSTRAINT pk_menu_order_count PRIMARY KEY (id)
);

CREATE TABLE menu_stocks
(
    id         BINARY(16) NOT NULL,
    deleted    BIT(1)   NOT NULL,
    deleted_at datetime NULL,
    created_at datetime NOT NULL,
    update_at  datetime NOT NULL,
    menu_id    BINARY(16) NOT NULL,
    quantity   BIGINT   NOT NULL,
    CONSTRAINT pk_menu_stocks PRIMARY KEY (id)
);

CREATE TABLE menus
(
    id               BINARY(16)   NOT NULL,
    deleted          BIT(1)       NOT NULL,
    deleted_at       datetime NULL,
    created_at       datetime     NOT NULL,
    update_at        datetime     NOT NULL,
    menu_category_id BINARY(16)   NOT NULL,
    name             VARCHAR(100) NOT NULL,
    price            BIGINT       NOT NULL,
    `description`    VARCHAR(350) NULL,
    sort_number      INT          NOT NULL,
    CONSTRAINT pk_menus PRIMARY KEY (id)
);

CREATE TABLE order_menu_options
(
    id                   BINARY(16)   NOT NULL,
    order_menu_id        BINARY(16)   NOT NULL,
    order_menu_option_id BINARY(16)   NOT NULL,
    name_snap            VARCHAR(100) NOT NULL,
    price_snap           BIGINT       NOT NULL,
    CONSTRAINT pk_order_menu_options PRIMARY KEY (id)
);

CREATE TABLE order_menus
(
    id          BINARY(16)   NOT NULL,
    order_id    BINARY(16)   NOT NULL,
    menu_id     BINARY(16)   NOT NULL,
    name_snap   VARCHAR(100) NOT NULL,
    price_snap  BIGINT       NOT NULL,
    quantity    BIGINT       NOT NULL,
    total_price BIGINT       NOT NULL,
    CONSTRAINT pk_order_menus PRIMARY KEY (id)
);

CREATE TABLE orders
(
    id          BINARY(16)   NOT NULL,
    member_id   BINARY(16)   NOT NULL,
    order_no    VARCHAR(100) NOT NULL,
    status      VARCHAR(30)  NOT NULL,
    total_price BIGINT       NOT NULL,
    order_at    datetime     NOT NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE TABLE point
(
    id         BINARY(16) NOT NULL,
    deleted    BIT(1)   NOT NULL,
    deleted_at datetime NULL,
    created_at datetime NOT NULL,
    update_at  datetime NOT NULL,
    member_id  BINARY(16) NOT NULL,
    balance    DECIMAL  NOT NULL,
    CONSTRAINT pk_point PRIMARY KEY (id)
);

CREATE TABLE point_logs
(
    id         BINARY(16)   NOT NULL,
    deleted    BIT(1)       NOT NULL,
    deleted_at datetime NULL,
    created_at datetime     NOT NULL,
    update_at  datetime     NOT NULL,
    member_id  BINARY(16)   NOT NULL,
    order_id   BINARY(16)   NULL,
    amount     DECIMAL      NOT NULL,
    type       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_point_logs PRIMARY KEY (id)
);

ALTER TABLE members
    ADD CONSTRAINT uk_members_email UNIQUE (email);

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

ALTER TABLE orders
    ADD CONSTRAINT uk_orders_order_no UNIQUE (order_no);

ALTER TABLE point
    ADD CONSTRAINT uk_point_id UNIQUE (id);

ALTER TABLE point_logs
    ADD CONSTRAINT uk_point_logs_id UNIQUE (id);