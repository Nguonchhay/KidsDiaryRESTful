package com.rupp.spring.service;

import java.util.List;

import com.rupp.spring.domain.Family;
import com.rupp.spring.domain.ResponseList;

public interface FamilyService {
    List<Family> list();
    List<Family> getFamily(Long parentId, Long userType);
    Family get(Long id);
    Family create(Family camily);
    Long delete(Long id);
    Family update(Long id, Family camily);
    void updateFamily(Long father, Long mother, boolean isFather);
    ResponseList<Family> getPage(int pagesize, String cursorkey);
}
