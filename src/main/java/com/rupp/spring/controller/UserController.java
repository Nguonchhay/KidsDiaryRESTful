package com.rupp.spring.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupp.spring.domain.User;
import com.rupp.spring.domain.ResponseList;
import com.rupp.spring.service.UserService;

@Controller
@RequestMapping("users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService service;
    

    //@RequestMapping(value = "/v1", method = RequestMethod.GET)
    @GetMapping("/v1/all")
    @ResponseBody
    public List<User> getUsers() {
        logger.debug("====get all users====");
        return service.list();
    }
    
    @GetMapping("/v1")
    @ResponseBody
    public ResponseList<User> getPage(@RequestParam(value="pagesize", defaultValue="10") int pagesize,
            @RequestParam(value = "cursorkey", required = false) String cursorkey) {
        logger.debug("====get page {} , {} ====", pagesize, cursorkey);
        return service.getPage(pagesize, cursorkey);
    }

    //@RequestMapping(value = "/v1/{id}", method = RequestMethod.GET)
    @GetMapping("/v1/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        logger.debug("====get country detail with id :[{}] ====", id);
        final User user = service.get(id);
        if (user == null) {
            return new ResponseEntity("No User found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/v1/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity login(HttpServletRequest request, 
            @RequestParam(value="username", required=true) String username, 
            @RequestParam(value="password", required=true) String password) {
    	logger.debug("====Login ====");
    	final User user = service.login(username, password);
        if (user == null) {
            return new ResponseEntity("Username or password is incorrect!", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
        
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    //@RequestMapping(value = "/v1", method = RequestMethod.POST)
    @PostMapping(value = "/v1")
    public ResponseEntity<User> createUser(@ModelAttribute User user) {
    	logger.debug("====create new user object ====");
        service.create(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @PutMapping("/v1/{id}")
    public ResponseEntity updateUser(@PathVariable Long id, @ModelAttribute User user) {
        logger.debug("====update user detail with id :[{}] ====", id);
        user = service.update(id, user);

        if (null == user) {
            return new ResponseEntity("No Country found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(user, HttpStatus.OK);
    }
    
    //@RequestMapping(value = "/v1/{id}", method = RequestMethod.DELETE)
    @DeleteMapping("/v1/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        logger.debug("====delete customer with id :[{}] ====", id);
        User user = service.get(id);
        if (user == null) {
            return new ResponseEntity("No user found for ID " + id, HttpStatus.NOT_FOUND);
        }
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date deletedAt = new Date();
        user.setDeletedAt(deletedAt);
        user = service.update(id, user);

        if (null == user) {
            return new ResponseEntity("No user found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity("Delete user: " + user.getFullname(), HttpStatus.OK);
    }
    
    //@RequestMapping(value = "/v1/force/{id}", method = RequestMethod.DELETE)
    @DeleteMapping("/v1/force/{id}")
    public ResponseEntity forceDeleteCountry(@PathVariable Long id) {
        logger.debug("====Force delete user with id :[{}] ====", id);
        if (null == service.delete(id)) {
            return new ResponseEntity("No user found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Force delete user with id = " + id, HttpStatus.OK);
    }
}
