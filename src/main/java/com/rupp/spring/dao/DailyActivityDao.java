package com.rupp.spring.dao;

import java.util.List;

import com.rupp.spring.domain.DailyActivity;
import com.rupp.spring.domain.ResponseList;

public interface DailyActivityDao {

    /**
     * @return
     */
    List<List> list(Long parent, Long child, int userType);

    /**
     * @param id
     * @return DailyActivity
     */
    DailyActivity get(Long id);
    
    /**
     * @param DailyActivity
     * @return DailyActivity
     */
    DailyActivity create(DailyActivity dailyActivity);
    
    /**
     * @param id
     * @return
     */
    Long delete(Long id);
    
    /**
     * @param dailyActivity
     * @return
     */
    DailyActivity update(DailyActivity dailyActivity);
    
    /**
     * @param pagesize
     * @param cursorkey
     * @return
     */
    ResponseList<DailyActivity> getPage(int pagesize, String cursorkey);
}
