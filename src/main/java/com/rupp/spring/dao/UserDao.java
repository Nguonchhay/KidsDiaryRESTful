package com.rupp.spring.dao;

import java.util.List;

import com.rupp.spring.domain.User;
import com.rupp.spring.domain.ResponseList;

public interface UserDao {

    /**
     * @return
     */
    List<User> list();

    /**
     * @param username
     * @param password
     * @return User
     */
    User login(String username, String password);
    
    /**
     * @param id
     * @return User
     */
    User get(Long id);
    
    /**
     * @param user
     * @return User
     */
    User create(User user);
    
    /**
     * @param id
     * @return
     */
    Long delete(Long id);
    
    /**
     * @param user
     * @return User
     */
    User update(User user);
    
    /**
     * @param pagesize
     * @param cursorkey
     * @return
     */
    ResponseList<User> getPage(int pagesize, String cursorkey);
}
