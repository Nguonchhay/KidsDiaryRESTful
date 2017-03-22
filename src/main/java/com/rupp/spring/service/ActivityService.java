package com.rupp.spring.service;

import java.util.List;

import com.rupp.spring.domain.Activity;
import com.rupp.spring.domain.ResponseList;

public interface ActivityService {
    List<Activity> list(Long parentId);
    Activity get(Long id);
    Activity create(Activity activity);
    Long delete(Long id);
    Activity update(Long id, Activity activity);
    ResponseList<Activity> getPage(int pagesize, String cursorkey);
}
