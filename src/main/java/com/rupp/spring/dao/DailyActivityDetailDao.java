package com.rupp.spring.dao;

import java.util.List;

import com.rupp.spring.domain.DailyActivity;
import com.rupp.spring.domain.ResponseList;

public interface DailyActivityDetailDao {

    /**
     * @return
     */
    List<DailyActivity> list();

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
