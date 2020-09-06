SET FOREIGN_KEY_CHECKS=0;

TRUNCATE TABLE users;
TRUNCATE TABLE roles;
TRUNCATE TABLE users_roles;
TRUNCATE TABLE companies;
TRUNCATE TABLE shops;
TRUNCATE TABLE products;
TRUNCATE TABLE products_shops;
TRUNCATE TABLE favorites;
TRUNCATE TABLE subscriptions;
TRUNCATE TABLE settings;

-- roles
INSERT INTO roles (id, name)
    VALUES (1, 'ROLE_ADMIN'),
    (2, 'ROLE_REGISTERED_USER');

-- users
INSERT INTO users (id, email, password, first_name, last_name)
    VALUES (1, 'admin1@market.com', '$2a$10$cV.qelWbracD69r9yjraf.a6YjWiqPvPOWa77noGn9YfZA36HM9Iu', 'Developer', 'Account 1'),
    (2, 'user1@market.com', '$2a$10$cV.qelWbracD69r9yjraf.a6YjWiqPvPOWa77noGn9YfZA36HM9Iu', 'Developer', 'Account 2'),
    (3, 'user2@market.com', '$2a$10$cV.qelWbracD69r9yjraf.a6YjWiqPvPOWa77noGn9YfZA36HM9Iu', 'Developer', 'Account 3');

-- users_roles
INSERT INTO users_roles (fk_user_id, fk_role_id)
    VALUES (1, 1),
    (2, 2),
    (3, 2);

-- settings
INSERT INTO settings (emails_enabled, location_tracking_allowed, search_radius, sync_enabled, fk_user_id, updated_at)
    VALUES (0, 0, 1, 0, 1, NOW()),
    (0, 0, 1, 0, 2, NOW()),
    (0, 0, 1, 0, 3, NOW());

-- companies
INSERT INTO companies (id, name, description, vat, rid, country, city, address, fk_user_id)
    VALUES (1, 'Company 1', 'Company description 1', 1234, 5678, 'Srbija', 'Novi Sad', 'Address 1', 2),
    (2, 'Company 2', 'Company description 2', 1234, 5678, 'Srbija', 'Novi Sad', 'Address 2', 3);

-- shops
INSERT INTO shops (id, name, description, latitude, longitude, fk_company_id)
    VALUES (1, 'Shop 1', 'Shop description 1', 45.305121, 19.831552, 1),
    (2, 'Shop 2', 'Shop description 1', 45.242029, 19.832790, 1);

-- products
INSERT INTO products (id, name, description, unit, price, fk_company_id)
    VALUES (1, 'Product 1', 'Product description 1', 'g', 99.99, 1),
    (2, 'Product 2', 'Product description 2', 'kg', 20.00, 1);

-- products_shops
INSERT INTO products_shops (fk_shop_id, fk_product_id)
    VALUES (1, 1),
    (1, 2);

-- favorites
INSERT INTO favorites (id, fk_shop_id, fk_user_id, updated_at, deleted)
    VALUES (1, 1, 2, NOW(), FALSE);

-- subscriptions
INSERT INTO subscriptions (id, fk_company_id, fk_user_id)
    VALUES (1, 1, 2);

SET FOREIGN_KEY_CHECKS=1;
