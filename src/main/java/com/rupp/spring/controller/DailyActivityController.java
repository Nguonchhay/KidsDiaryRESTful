package com.rupp.spring.controller;

import java.text.DateFormat;
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

import com.rupp.spring.domain.DailyActivity;
import com.rupp.spring.domain.ResponseList;
import com.rupp.spring.service.DailyActivityService;
import com.rupp.spring.service.DailyActivityDetailService;

@Controller
@RequestMapping("dailyactivity")
public class DailyActivityController {
    private static final Logger logger = LoggerFactory.getLogger(DailyActivityController.class);

    @Autowired
    private DailyActivityService service;
    
    @Autowired
    private DailyActivityDetailService detailService;
    

    //@RequestMapping(value = "/v1", method = RequestMethod.GET)
    @GetMapping("/v1/all")
    @ResponseBody
    public List<DailyActivity> getCountries(@RequestParam(value="parent") Long parent,
    		@RequestParam(value="child") Long child,
    		@RequestParam(value="userType") Long userType) {
        logger.debug("====get all daily activities====");
        return service.list(parent, child, userType);
    }
    
    @GetMapping("/v1")
    @ResponseBody
    public ResponseList<DailyActivity> getPage(@RequestParam(value="pagesize", defaultValue="10") int pagesize,
            @RequestParam(value = "cursorkey", required = false) String cursorkey) {
        logger.debug("====get page {} , {} ====", pagesize, cursorkey);
        return service.getPage(pagesize, cursorkey);
    }

    //@RequestMapping(value = "/v1/{id}", method = RequestMethod.GET)
    @GetMapping("/v1/{id}")
    public ResponseEntity<DailyActivity> getDailyActivity(@PathVariable("id") Long id) {
        logger.debug("====get dailyActivity detail with id :[{}] ====", id);
        final DailyActivity dailyActivity = service.get(id);
        if (dailyActivity == null) {
            return new ResponseEntity("No DailyActivity found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dailyActivity, HttpStatus.OK);
    }
    
  //@RequestMapping(value = "/v1", method = RequestMethod.POST)
    @PostMapping(value = "/v1")
    public ResponseEntity<DailyActivity> createDailyActivity(@ModelAttribute DailyActivity dailyActivity) {
        logger.debug("====create new dailyActivity object ====");
        service.create(dailyActivity);
        return new ResponseEntity<>(dailyActivity, HttpStatus.OK);
    }
    
    @PutMapping("/v1/{id}")
    public ResponseEntity updateDailyActivity(@PathVariable Long id, @ModelAttribute DailyActivity dailyActivity) {
        logger.debug("====update dailyActivity detail with id :[{}] ====", id);
        dailyActivity = service.update(id, dailyActivity);

        if (null == dailyActivity) {
            return new ResponseEntity("No DailyActivity found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(dailyActivity, HttpStatus.OK);
    }
    
    //@RequestMapping(value = "/v1/{id}", method = RequestMethod.DELETE)
    @DeleteMapping("/v1/{id}")
    public ResponseEntity deleteDailyActivity(@PathVariable Long id) {
        logger.debug("====delete dailyActivity with id :[{}] ====", id);
        DailyActivity dailyActivity = service.get(id);
        if (dailyActivity == null) {
            return new ResponseEntity("No DailyActivity found for ID " + id, HttpStatus.NOT_FOUND);
        }
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date deletedAt = new Date();
        dailyActivity.setDeletedAt(deletedAt);
        dailyActivity = service.update(id, dailyActivity);

        if (null == dailyActivity) {
            return new ResponseEntity("No DailyActivity found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity("Delete dailyActivity: " + dailyActivity.getName(), HttpStatus.OK);
    }
    
    //@RequestMapping(value = "/v1/force/{id}", method = RequestMethod.DELETE)
    @DeleteMapping("/v1/force/{id}")
    public ResponseEntity forceDeleteDailyActivity(@PathVariable Long id) {
        logger.debug("====Force delete dailyActivity with id :[{}] ====", id);
        if (null == service.delete(id)) {
            return new ResponseEntity("No dailyActivity found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Force delete dailyActivity with id = " + id, HttpStatus.OK);
    }
}
