package com.gingeroy.ministoreapi.repositories;

import com.gingeroy.ministoreapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
//    CrudRepository<User, Long> - 返回的是可迭代对象 Iterable<User>
//    JpaRepository<User, Long> - 返回的是列表 List<User>

    boolean existsByEmail(String email);

//    Optional 不是集合，也不是实体类。
//    它是 Java 8 引入的一个容器类，放在 java.util 包里。
//    可以理解成：一个只能装 0 个或 1 个元素的盒子。
    Optional<User> findByEmail(String email);
}
