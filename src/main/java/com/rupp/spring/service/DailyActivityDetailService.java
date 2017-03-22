package com.rupp.spring.service;

import java.util.List;

import com.rupp.spring.domain.DailyActivity;
import com.rupp.spring.domain.ResponseList;

public interface DailyActivityDetailService {
    List<DailyActivity> list();
    DailyActivity get(Long id);
    DailyActivity create(DailyActivity dailyActivity);
    Long delete(Long id);
    DailyActivity update(Long id, DailyActivity dailyActivity);
    ResponseList<DailyActivity> getPage(int pagesize, String cursorkey);
}
