DROP TABLE IF EXISTS menu_options;

DROP TABLE IF EXISTS menu_option_categories;

DROP TABLE IF EXISTS menu_categories;

ALTER TABLE menus
    DROP COLUMN menu_category_id;
