package com.backblog.service;

import com.backblog.po.User;

public interface UserService {

    User checkUser(String username, String password);
}
