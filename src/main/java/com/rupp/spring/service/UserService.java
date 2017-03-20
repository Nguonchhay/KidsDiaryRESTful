package com.rupp.spring.service;

import java.util.List;

import com.rupp.spring.domain.User;
import com.rupp.spring.domain.ResponseList;

public interface UserService {
    List<User> list();
    User login(String username, String password);
    User get(Long id);
    User create(User user);
    Long delete(Long id);
    User update(Long id, User user);
    ResponseList<User> getPage(int pagesize, String cursorkey);
}
