package com.rupp.spring.dao;

import java.util.List;

import com.rupp.spring.domain.Activity;
import com.rupp.spring.domain.ResponseList;

public interface ActivityDao {

    /**
     * @param parentId
     * @return
     */
    List<Activity> list(Long parentId);

    /**
     * @param id
     * @return Activity
     */
    Activity get(Long id);
    
    /**
     * @param Activity
     * @return Activity
     */
    Activity create(Activity activity);
    
    /**
     * @param id
     * @return
     */
    Long delete(Long id);
    
    /**
     * @param Activity
     * @return
     */
    Activity update(Activity activity);
    
    /**
     * @param pagesize
     * @param cursorkey
     * @return
     */
    ResponseList<Activity> getPage(int pagesize, String cursorkey);
}
