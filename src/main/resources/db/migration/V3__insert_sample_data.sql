-- 示例数据：分类与商品。
-- 购物车和用户由接口创建，不在迁移脚本里写死。
insert into categories (id, name)
values (1, 'Electronics'),
       (2, 'Books'),
       (3, 'Home');

insert into products (id, name, price, `description`, category_id)
values (1, 'Wireless Mouse', 59.90, 'A compact wireless mouse.', 1),
       (2, 'Mechanical Keyboard', 299.00, 'A keyboard with tactile switches.', 1),
       (3, 'Java Programming Book', 79.00, 'An introductory Java book.', 2),
       (4, 'Desk Lamp', 89.50, 'An adjustable LED desk lamp.', 3),
       (5, 'Coffee Mug', 25.00, 'A ceramic coffee mug.', 3),
       (6, 'Notebook', 12.80, 'A 100-page notebook.', 2);


-- 示例用户（仅用于本地演示）。
-- 三个账号的明文密码都是 demo123456；这里存的是 BCrypt 哈希，不是明文，
-- 与 SecurityConfig 里的 BCryptPasswordEncoder 对应，可以直接用 /auth/login 登录。
insert into users (name, email, password)
values ('Alice', 'alice@example.com', '$2a$10$.vKVYX4z3lfhKVp7ko.0Uu2aPF1Dt8.us3rbBsGK3XTYcMM9vQfQ2'),
       ('Bob',   'bob@example.com',   '$2a$10$2ObfwfccGVI64AWbzXhNG.O2mfYYMBuAAHXupwPQKLs/pOgDf9OGu'),
       ('Carol', 'carol@example.com', '$2a$10$3HodrUildCLfJEbMD6858uz1ipzL6YTvhw2VEuF4YysFPh2XMu356');