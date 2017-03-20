package com.rupp.spring.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupp.spring.dao.UserDao;
import com.rupp.spring.domain.User;
import com.rupp.spring.domain.ResponseList;

@Service("userService")
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserDao dao;
    
    @Override
    public List<User> list() {
        return dao.list();
    }
    
    @Override
    public User login(String username, String password) {
        return dao.login(username, password);
    }

    @Override
    public User get(Long id) {
        return dao.get(id);
    }

    @Override
    public User create(User user) {
        return dao.create(user);
    }

    @Override
    public Long delete(Long id) {
        return dao.delete(id);
    }

    @Override
    public User update(Long id, User user) {
        if (dao.get(id) == null) {
            return null;
        }
        user.setId(id);
        return dao.update(user);
    }
    
    public ResponseList<User> getPage(int pagesize, String cursorkey) {
        logger.debug(" getPage limit : {} cursorkey : {}", pagesize, cursorkey);
        return dao.getPage(pagesize, cursorkey);
    }
}
