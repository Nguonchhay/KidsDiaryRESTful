package com.rupp.spring.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupp.spring.dao.DailyActivityDao;
import com.rupp.spring.domain.DailyActivity;
import com.rupp.spring.domain.ResponseList;

@Service("dailyActivityService")
public class DailyActivityServiceImpl implements DailyActivityService {
    private static final Logger logger = LoggerFactory.getLogger(DailyActivityServiceImpl.class);
    
    @Autowired
    private DailyActivityDao dao;
    
    @Override
    public List<DailyActivity> list(Long parent, Long child, int userType) {
        return dao.list(parent, child, userType);
    }
    

    @Override
    public DailyActivity get(Long id) {
        return dao.get(id);
    }

    @Override
    public DailyActivity create(DailyActivity dailyActivity) {
        return dao.create(dailyActivity);
    }

    @Override
    public Long delete(Long id) {
        return dao.delete(id);
    }

    @Override
    public DailyActivity update(Long id, DailyActivity dailyActivity) {
        if (dao.get(id) == null) {
            return null;
        }
        dailyActivity.setId(id);
        return dao.update(dailyActivity);
    }
    
    public ResponseList<DailyActivity> getPage(int pagesize, String cursorkey) {
        logger.debug(" getPage limit : {} cursorkey : {}", pagesize, cursorkey);
        return dao.getPage(pagesize, cursorkey);
    }
}
