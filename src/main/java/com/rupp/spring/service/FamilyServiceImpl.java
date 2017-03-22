package com.rupp.spring.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupp.spring.dao.FamilyDao;
import com.rupp.spring.domain.Family;
import com.rupp.spring.domain.ResponseList;

@Service("familyService")
public class FamilyServiceImpl implements FamilyService {
    private static final Logger logger = LoggerFactory.getLogger(FamilyServiceImpl.class);
    
    @Autowired
    private FamilyDao dao;
    
    @Override
    public List<Family> list() {
        return dao.list();
    }
    
    @Override
    public List<Family> getFamily(Long parentId, Long userType) {
    	return dao.getFamily(parentId, userType);
    }

    @Override
    public Family get(Long id) {
        return dao.get(id);
    }

    @Override
    public Family create(Family family) {
        return dao.create(family);
    }

    @Override
    public Long delete(Long id) {
        return dao.delete(id);
    }
    
    @Override
    public boolean deleteChild(Long parentId, Long childId, Long userType) {
    	return dao.deleteChild( parentId, childId, userType);
    }

    @Override
    public Family update(Long id, Family family) {
        
        if (dao.get(id) == null) {
            return null;
        }
        family.setId(id);
        return dao.update(family);
    }
    
    @Override
    public void updateFamily(Long father, Long mother, boolean isFather) {
        dao.updateFamily(father, mother, isFather);
    }
    
    public ResponseList<Family> getPage(int pagesize, String cursorkey) {
        logger.debug(" getPage limit : {} cursorkey : {}", pagesize, cursorkey);
        return dao.getPage(pagesize, cursorkey);
    }
}
