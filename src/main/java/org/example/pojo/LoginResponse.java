package org.example.pojo;

import lombok.Data;

@Data
public class LoginResponse {
    String token;
    String userId;
    String message;
}
