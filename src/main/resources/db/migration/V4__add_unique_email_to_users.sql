-- 给 users.email 加唯一约束，防止并发注册时出现重复邮箱。
-- 执行前请先确认本地没有重复邮箱：
--   select email, count(*) from users group by email having count(*) > 1;
alter table users
    add constraint users_email_unique unique (email);