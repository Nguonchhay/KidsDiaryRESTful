package com.rupp.spring.dao;

import java.util.List;

import com.rupp.spring.domain.Family;
import com.rupp.spring.domain.ResponseList;

public interface FamilyDao {

    /**
     * @return
     */
    List<Family> list();
    
    /**
     * @return
     */
    List<Family> getFamily(Long parentId, Long userType);

    /**
     * @param id
     * @return Family
     */
    Family get(Long id);
    
    /**
     * @param Family
     * @return Family
     */
    Family create(Family family);
    
    /**
     * @param id
     * @return
     */
    Long delete(Long id);
    
    /**
     * @param parentId
     * @param childId
     * @return
     */
    boolean deleteChild(Long parentId, Long childId, Long userType);
    
    /**
     * @param family
     * @return
     */
    Family update(Family family);
    
    /**
     * @param father
     * @param mother
     * @param isFather
     */
    void updateFamily(Long father, Long mother, boolean isFather);
    
    /**
     * @param pagesize
     * @param cursorkey
     * @return
     */
    ResponseList<Family> getPage(int pagesize, String cursorkey);
}
