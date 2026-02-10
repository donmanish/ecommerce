package com.userapi.eccomerceone.service;

import com.userapi.eccomerceone.model.User;

public interface UserService {
    User register(User user);
    User findByEmail(String email);
}
