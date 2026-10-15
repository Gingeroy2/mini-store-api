package com.gingeroy.ministoreapi.mappers;

import com.gingeroy.ministoreapi.dtos.RegisterUserRequest;
import com.gingeroy.ministoreapi.dtos.UpdateUserRequest;
import com.gingeroy.ministoreapi.dtos.UserDto;
import com.gingeroy.ministoreapi.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

// spring 可以在运行时创建这种类型的 Bean
@Mapper(componentModel = "spring")
public interface UserMapper {
//    mapstruct 将自动实现此接口，可以声明各种映射方法

//    将用户映射到用户DTO
//    `ignore = true` - 映射的时候跳过这个字段，不赋值
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
//    代表：映射到 `UserDto` 的 `createdAt` 字段时，执行 Java 代码 `LocalDateTime.now()` 赋值，不从源对象 `User` 读取任何值
    UserDto toUserDto(User user);

    User toEntity(RegisterUserRequest request);

    void update(UpdateUserRequest request, @MappingTarget User user);

}
