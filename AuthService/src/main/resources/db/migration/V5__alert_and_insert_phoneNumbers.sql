ALTER TABLE users
ADD COLUMN phone_number VARCHAR(20);

UPDATE users SET phone_number = '79952501672' WHERE username = 'user';
UPDATE users SET phone_number = '79800968375' WHERE username = 'admin';
