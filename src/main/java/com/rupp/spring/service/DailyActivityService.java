package com.rupp.spring.service;

import java.util.List;

import com.rupp.spring.domain.DailyActivity;
import com.rupp.spring.domain.ResponseList;

public interface DailyActivityService {
    List<DailyActivity> list(Long parent, Long child, int userType);
    DailyActivity get(Long id);
    DailyActivity create(DailyActivity dailyActivity);
    Long delete(Long id);
    DailyActivity update(Long id, DailyActivity dailyActivity);
    ResponseList<DailyActivity> getPage(int pagesize, String cursorkey);
}
