package com.gingeroy.ministoreapi.services;

import com.gingeroy.ministoreapi.entities.User;
import com.gingeroy.ministoreapi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

//    通过用户名加载用户
//    当用户登录时，Spring Security 会调用它，根据用户输入的 email 去数据库查用户，然后把用户信息交给 Spring Security 去比对密码。
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList());
        /*
        这是整段代码最核心的“转换”步骤。
        你数据库查出来的 User 是你自己写的实体类（包含 id、email、password 等）。
        但 Spring Security 只认识它自己定义的 UserDetails 接口。
        所以这里 new org.springframework.security.core.userdetails.User(...) 就是在做适配器。
            参数 1：用户名（email）
            参数 2：数据库里存的加密密码（框架接下来会拿这个密码去比对）
            参数 3：权限列表（这里先给了 Collections.emptyList()，表示暂时没设置角色/权限）。
        */
    }
}
