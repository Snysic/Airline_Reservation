INSERT INTO roles (name) VALUES ('ROLE_ADMIN'), ('ROLE_USER');

INSERT INTO users (username, password) 
VALUES ('admin', '$2a$12$hashedpassword'), 
       ('user', '$2a$12$hashedpassword');

INSERT INTO roles_users (role_id, user_id) 
VALUES (1, 1), 
       (2, 2);