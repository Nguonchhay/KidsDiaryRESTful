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

import com.rupp.spring.domain.Activity;
import com.rupp.spring.domain.ResponseList;
import com.rupp.spring.service.ActivityService;

@Controller
@RequestMapping("activities")
public class ActivityController {
    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private ActivityService service;
    

    //@RequestMapping(value = "/v1", method = RequestMethod.GET)
    @GetMapping("/v1/all")
    @ResponseBody
    public List<Activity> getCountries(@RequestParam(value="parentId") Long parentId) {
        logger.debug("====get all activities====");
        return service.list(parentId);
    }
    
    @GetMapping("/v1")
    @ResponseBody
    public ResponseList<Activity> getPage(@RequestParam(value="pagesize", defaultValue="10") int pagesize,
            @RequestParam(value = "cursorkey", required = false) String cursorkey) {
        logger.debug("====get page {} , {} ====", pagesize, cursorkey);
        return service.getPage(pagesize, cursorkey);
    }

    //@RequestMapping(value = "/v1/{id}", method = RequestMethod.GET)
    @GetMapping("/v1/{id}")
    public ResponseEntity<Activity> getActivity(@PathVariable("id") Long id) {
        logger.debug("====get activity detail with id :[{}] ====", id);
        final Activity activity = service.get(id);
        if (activity == null) {
            return new ResponseEntity("No Activity found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(activity, HttpStatus.OK);
    }
    
  //@RequestMapping(value = "/v1", method = RequestMethod.POST)
    @PostMapping(value = "/v1")
    public ResponseEntity<Activity> createActivity(@ModelAttribute Activity activity) {
        logger.debug("====create new activity object ====");
        service.create(activity);
        return new ResponseEntity<>(activity, HttpStatus.OK);
    }
    
    @PutMapping("/v1/{id}")
    public ResponseEntity updateActivity(@PathVariable Long id, @ModelAttribute Activity activity) {
        logger.debug("====update activity detail with id :[{}] ====", id);
        activity = service.update(id, activity);

        if (null == activity) {
            return new ResponseEntity("No Activity found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(activity, HttpStatus.OK);
    }
    
    //@RequestMapping(value = "/v1/{id}", method = RequestMethod.DELETE)
    @DeleteMapping("/v1/{id}")
    public ResponseEntity deleteActivity(@PathVariable Long id) {
        logger.debug("====delete activity with id :[{}] ====", id);
        Activity activity = service.get(id);
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
        return new ResponseEntity("Delete activity: " + activity.getName(), HttpStatus.OK);
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
