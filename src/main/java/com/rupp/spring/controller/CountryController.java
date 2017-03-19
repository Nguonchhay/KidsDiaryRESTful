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

import com.rupp.spring.domain.Country;
import com.rupp.spring.domain.ResponseList;
import com.rupp.spring.service.CountryService;

@Controller
@RequestMapping("countries")
public class CountryController {
    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);

    @Autowired
    private CountryService service;
    

    //@RequestMapping(value = "/v1", method = RequestMethod.GET)
    @GetMapping("/v1/all")
    @ResponseBody
    public List<Country> getCountries() {
        logger.debug("====get all countries====");
        return service.list();
    }
    
    @GetMapping("/v1")
    @ResponseBody
    public ResponseList<Country> getPage(@RequestParam(value="pagesize", defaultValue="10") int pagesize,
            @RequestParam(value = "cursorkey", required = false) String cursorkey) {
        logger.debug("====get page {} , {} ====", pagesize, cursorkey);
        return service.getPage(pagesize, cursorkey);
    }

    //@RequestMapping(value = "/v1/{id}", method = RequestMethod.GET)
    @GetMapping("/v1/{id}")
    public ResponseEntity<Country> getCountry(@PathVariable("id") Long id) {
        logger.debug("====get country detail with id :[{}] ====", id);
        final Country country = service.get(id);
        if (country == null) {
            return new ResponseEntity("No Country found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(country, HttpStatus.OK);
    }
    
  //@RequestMapping(value = "/v1", method = RequestMethod.POST)
    @PostMapping(value = "/v1")
    public ResponseEntity<Country> createCountry(@ModelAttribute Country country) {
        logger.debug("====create new country object ====");
        service.create(country);
        return new ResponseEntity<>(country, HttpStatus.OK);
    }
    
    @PutMapping("/v1/{id}")
    public ResponseEntity updateCountry(@PathVariable Long id, @ModelAttribute Country country) {
        logger.debug("====update country detail with id :[{}] ====", id);
        country = service.update(id, country);

        if (null == country) {
            return new ResponseEntity("No Country found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(country, HttpStatus.OK);
    }
    
    //@RequestMapping(value = "/v1/{id}", method = RequestMethod.DELETE)
    @DeleteMapping("/v1/{id}")
    public ResponseEntity deleteCountry(@PathVariable Long id) {
        logger.debug("====delete country with id :[{}] ====", id);
        Country country = service.get(id);
        if (country == null) {
            return new ResponseEntity("No Country found for ID " + id, HttpStatus.NOT_FOUND);
        }
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date deletedAt = new Date();
        country.setDeletedAt(deletedAt);
        country = service.update(id, country);

        if (null == country) {
            return new ResponseEntity("No Country found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity("Delete country: " + country.getName(), HttpStatus.OK);
    }
    
    //@RequestMapping(value = "/v1/force/{id}", method = RequestMethod.DELETE)
    @DeleteMapping("/v1/force/{id}")
    public ResponseEntity forceDeleteCountry(@PathVariable Long id) {
        logger.debug("====Force delete country with id :[{}] ====", id);
        if (null == service.delete(id)) {
            return new ResponseEntity("No country found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Force delete country with id = " + id, HttpStatus.OK);
    }
}
