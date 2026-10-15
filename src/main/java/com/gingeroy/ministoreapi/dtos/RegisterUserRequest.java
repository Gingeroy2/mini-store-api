package com.gingeroy.ministoreapi.dtos;

import com.gingeroy.ministoreapi.validation.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @NotBlank // 确保不是空字符串或空白
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotBlank
    @Email(message = "Email must be valid")
    @Lowercase(message = "Email must be lowercase")
    private String email;

    @NotBlank
    @Size(min = 6, max = 25)
    private String password;
}
