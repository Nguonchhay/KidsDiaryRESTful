package com.rupp.spring.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupp.spring.dao.ActivityDao;
import com.rupp.spring.domain.Activity;
import com.rupp.spring.domain.ResponseList;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityServiceImpl.class);
    
    @Autowired
    private ActivityDao dao;
    
    @Override
    public List<Activity> list(Long parentId) {
        return dao.list(parentId);
    }

    @Override
    public Activity get(Long id) {
        return dao.get(id);
    }

    @Override
    public Activity create(Activity activity) {
        return dao.create(activity);
    }

    @Override
    public Long delete(Long id) {
        return dao.delete(id);
    }

    @Override
    public Activity update(Long id, Activity activity) {
        
        if (dao.get(id) == null) {
            return null;
        }
        activity.setId(id);
        return dao.update(activity);
    }
    
    public ResponseList<Activity> getPage(int pagesize, String cursorkey) {
        logger.debug(" getPage limit : {} cursorkey : {}", pagesize, cursorkey);
        return dao.getPage(pagesize, cursorkey);
    }
}
