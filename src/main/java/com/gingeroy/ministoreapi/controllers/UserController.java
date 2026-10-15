package com.gingeroy.ministoreapi.controllers;


import com.gingeroy.ministoreapi.dtos.ChangePasswordRequest;
import com.gingeroy.ministoreapi.dtos.RegisterUserRequest;
import com.gingeroy.ministoreapi.dtos.UpdateUserRequest;
import com.gingeroy.ministoreapi.dtos.UserDto;
import com.gingeroy.ministoreapi.entities.User;
import com.gingeroy.ministoreapi.mappers.UserMapper;
import com.gingeroy.ministoreapi.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public List<UserDto> getAllUsers
    (
            @RequestHeader(required = false, name = "x-auth-token") String authToken,
            @RequestParam(required = false, defaultValue = "", name = "sort") String sort
    ) {

        if (!Set.of("name", "email").contains(sort)) {
//            将 name 设置为默认排序规则
            sort = "name";
        }

        return userRepository.findAll(Sort.by(sort))
                .stream()
                .map(user -> userMapper.toUserDto(user))
                .toList();
    }

    @GetMapping("/{id}")
//    使用响应实体 ResponseEntity，可以自定义响应
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
//            相当于 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            调用 build() 来获取一个 ResponseEntity 响应实体对象
        }

        return ResponseEntity.ok(userMapper.toUserDto(user));
        /*没有 userMapping 时 -
        UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
        return ResponseEntity.ok(userDto);*/
//        相当于 return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> registerUser
    (
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        if (userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest()
                    .body(Map.of("email","Email already exists"));
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = userRepository.save(user);
        UserDto userDto =  userMapper.toUserDto(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateUserRequest request) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null){
            return ResponseEntity.notFound().build();
        }

        userMapper.update(request, user);
        userRepository.save(user);
        UserDto userDto = userMapper.toUserDto(user);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        User user = userRepository.findById(id).orElse(null);
        if (user == null){
            return ResponseEntity.notFound().build();
        }

        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request){
        User user = userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }

        if (!user.getPassword().equals(request.getOldPassword())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }

}
