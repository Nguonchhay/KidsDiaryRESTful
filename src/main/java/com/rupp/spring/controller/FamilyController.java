package com.rupp.spring.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupp.spring.domain.Family;
import com.rupp.spring.domain.ResponseList;
import com.rupp.spring.domain.User;
import com.rupp.spring.service.FamilyService;
import com.rupp.spring.service.UserService;

@Controller
@RequestMapping("families")
public class FamilyController {
    private static final Logger logger = LoggerFactory.getLogger(FamilyController.class);

    @Autowired
    private FamilyService service;
    
    @Autowired
    private UserService userService;
    

    //@RequestMapping(value = "/v1", method = RequestMethod.GET)
    @GetMapping("/v1/all")
    @ResponseBody
    public List<Family> getFamilies() {
        logger.debug("====get all familites ====");
        return service.list();
    }
    
    @GetMapping("/v1")
    @ResponseBody
    public ResponseList<Family> getPage(@RequestParam(value="pagesize", defaultValue="10") int pagesize,
            @RequestParam(value = "cursorkey", required = false) String cursorkey) {
        logger.debug("====get page {} , {} ====", pagesize, cursorkey);
        return service.getPage(pagesize, cursorkey);
    }

    //@RequestMapping(value = "/v1/{id}", method = RequestMethod.GET)
    @GetMapping("/v1/{id}")
    public ResponseEntity<Family> getFamily(@PathVariable("id") Long id) {
        logger.debug("====get family detail with id :[{}] ====", id);
        final Family family = service.get(id);
        if (family == null) {
            return new ResponseEntity("No Family found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(family, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/v1", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity createFamily(HttpServletRequest request, 
    		@RequestParam(value="parentId", required=true) Long parentId,
    		@RequestParam(value="parentType", required=true) int parentType,
    		@RequestParam(value="note", required=true) String note,
            @RequestParam(value="firstName", required=true) String firstName, 
            @RequestParam(value="lastName", required=true) String lastName,
            @RequestParam(value="sex", required=true) String sex, 
            @RequestParam(value="username", required=true) String username,
            @RequestParam(value="password", required=true) String password, 
            @RequestParam(value="country", required=true) int country,
            @RequestParam(value="userType", required=true) int userType, 
            @RequestParam(value="birthDate", required=true) String birthDate,
            @RequestParam(value="email", required=true) String email, 
            @RequestParam(value="phone", required=true) String phone
    ) {
        logger.debug("====create new family object ====");
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setSex(sex);
        user.setUsername(username);
        user.setPassword(password);
        user.setCountry(country);
        user.setUserType(userType);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAccessToken("");
        user.setActivated(true);
        
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date inputDate;
			inputDate = dateFormat.parse(birthDate);
			user.setBirthDate(inputDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        user = userService.create(user);
        
        Family family = new Family();
        family.setChild(user.getId());
        family.setNote(note);
        boolean isFather = false;
        if (parentType == 1 || parentType == 4) {
        	family.setFather(parentId);
        	family.setMother(0L);
        	isFather = true;
        } else if (parentType == 2) {
        	family.setMother(parentId);
        	family.setFather(0L);
        }
        family = service.create(family);
        
        if (user.getUserType() != 3) {
        	service.updateFamily(family.getFather(), family.getMother(), isFather);
        }
        
        return new ResponseEntity<>(family, HttpStatus.OK);
    }
    
    @PutMapping("/v1/{id}")
    public ResponseEntity updateFamily(@PathVariable Long id, @ModelAttribute Family family) {
        logger.debug("====update familty detail with id :[{}] ====", id);
        family = service.update(id, family);

        if (null == family) {
            return new ResponseEntity("No Family found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(family, HttpStatus.OK);
    }
    
    //@RequestMapping(value = "/v1/{id}", method = RequestMethod.DELETE)
    @DeleteMapping("/v1/{id}")
    public ResponseEntity deleteFamily(@PathVariable Long id) {
        logger.debug("====delete family with id :[{}] ====", id);
        Family family = service.get(id);
        if (family == null) {
            return new ResponseEntity("No Family found for ID " + id, HttpStatus.NOT_FOUND);
        }
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date deletedAt = new Date();
        family.setDeletedAt(deletedAt);
        family = service.update(id, family);

        if (null == family) {
            return new ResponseEntity("No Family found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity("Delete family with id = " + id, HttpStatus.OK);
    }
    
    //@RequestMapping(value = "/v1/force/{id}", method = RequestMethod.DELETE)
    @DeleteMapping("/v1/force/{id}")
    public ResponseEntity forceDeleteFamily(@PathVariable Long id) {
        logger.debug("====Force delete family with id :[{}] ====", id);
        if (null == service.delete(id)) {
            return new ResponseEntity("No family found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Force delete family with id = " + id, HttpStatus.OK);
    }
}
