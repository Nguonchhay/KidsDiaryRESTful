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

@Controller
@RequestMapping("dailyactivities")
public class DailyActivityController {
    private static final Logger logger = LoggerFactory.getLogger(DailyActivityController.class);

    @Autowired
    private DailyActivityService service;
    

    //@RequestMapping(value = "/v1", method = RequestMethod.GET)
    @GetMapping("/v1/all")
    @ResponseBody
    public List<DailyActivity> getCountries(@RequestParam(value="parent") Long parent,
    		@RequestParam(value="userType") int userType,
    		@RequestParam(value="child") Long child) {
        logger.debug("====get all DailyActivity====");
        return service.list(parent, userType, child);
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
    public ResponseEntity<DailyActivity> getActivity(@PathVariable("id") Long id) {
        logger.debug("====get DailyActivity detail with id :[{}] ====", id);
        final DailyActivity activity = service.get(id);
        if (activity == null) {
            return new ResponseEntity("No Activity found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(activity, HttpStatus.OK);
    }
    
  //@RequestMapping(value = "/v1", method = RequestMethod.POST)
    @PostMapping(value = "/v1")
    public ResponseEntity<DailyActivity> createActivity(@ModelAttribute DailyActivity dailyActivity) {
        logger.debug("====create new DailyActivity object ====");
        service.create(dailyActivity);
        return new ResponseEntity<>(dailyActivity, HttpStatus.OK);
    }
    
    @PutMapping("/v1/{id}")
    public ResponseEntity updateActivity(@PathVariable Long id, @ModelAttribute DailyActivity dailyActivity) {
        logger.debug("====update activity detail with id :[{}] ====", id);
        dailyActivity = service.update(id, dailyActivity);

        if (null == dailyActivity) {
            return new ResponseEntity("No dailyActivity found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(dailyActivity, HttpStatus.OK);
    }
    
    //@RequestMapping(value = "/v1/{id}", method = RequestMethod.DELETE)
    @DeleteMapping("/v1/{id}")
    public ResponseEntity deleteActivity(@PathVariable Long id) {
        logger.debug("====delete activity with id :[{}] ====", id);
        DailyActivity activity = service.get(id);
        if (activity == null) {
            return new ResponseEntity("No Activity found for ID " + id, HttpStatus.NOT_FOUND);
        }
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date deletedAt = new Date();
        activity.setDeletedAt(deletedAt);
        activity = service.update(id, activity);

        if (null == activity) {
            return new ResponseEntity("No Activity found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity("Delete activity: ", HttpStatus.OK);
    }
    
    //@RequestMapping(value = "/v1/force/{id}", method = RequestMethod.DELETE)
    @DeleteMapping("/v1/force/{id}")
    public ResponseEntity forceDeleteActivity(@PathVariable Long id) {
        logger.debug("====Force delete activity with id :[{}] ====", id);
        if (null == service.delete(id)) {
            return new ResponseEntity("No activity found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Force delete activity with id = " + id, HttpStatus.OK);
    }
}
