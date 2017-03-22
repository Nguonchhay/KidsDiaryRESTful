package com.rupp.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.rupp.spring.domain.Family;
import com.rupp.spring.domain.ResponseList;
import com.rupp.spring.domain.User;

@Repository("familyDaoImpl")
public class FamilyDaoImpl implements FamilyDao {
    private static final Logger LOG = LoggerFactory.getLogger(FamilyDaoImpl.class);
    
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public FamilyDaoImpl(DataSource dataSource) {
        //jdbcTemplate = new JdbcTemplate(DBCP2DataSourceUtils.getDataSource());
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public int count() {
        final String sql = "select count(*) from " + Family.TABLE + " WHERE deletedAt IS NULL";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public ResponseList<Family> getPage(int pagesize, String offset) {
        if (offset == null) {
            offset = "0";
        }
        final String sql = "select * from " + Family.TABLE + " WHERE deletedAt IS NULL limit " + pagesize + " OFFSET " + offset;
        List<Family> list = this.jdbcTemplate.query(sql, new RowMapper<Family>() {

            @Override
            public Family mapRow(ResultSet rs, int paramInt) throws SQLException {
                final Family domain = new Family();
                domain.setId(rs.getLong("id"));
                domain.setFather(rs.getLong("father"));
                domain.setMother(rs.getLong("mother"));
                domain.setChild(rs.getLong("child"));
                domain.setNote(rs.getString("note"));
                domain.setCreatedAt(new Date(rs.getTimestamp("createdAt").getTime()));
                return domain;
            }
            
        });
        
        return populatePages(list, pagesize, String.valueOf(offset));
    }
    
    protected ResponseList<Family> populatePages(final Collection<Family> items, final int pageSize, final String cursorKey) {
        return populatePages(items, pageSize, cursorKey, null);
    }

    protected ResponseList<Family> populatePages(final Collection<Family> items, final int pageSize, final String cursorKey, final Integer totalCount) {

        if (items == null || items.isEmpty()) {
            return new ResponseList<Family>(items);
        }

        int total;
        if (totalCount == null) {
            total = count();
        } else {
            total = totalCount;
        }

        if ("0".equals(cursorKey) && items.size() < pageSize) {
            total = items.size();
        }

        // limit = data.size();
        LOG.debug(" total records count : {} : Integer.parseInt(cursorKey) + items.size() : {} ", total,
                Integer.parseInt(cursorKey) + items.size());
        String nextCursorKey = null;

        if (items.size() == pageSize && Integer.parseInt(cursorKey) + items.size() < total) {
            nextCursorKey = String.format("%s", Integer.parseInt(cursorKey) + items.size());
        }

        LOG.debug("next cursorKey {}", nextCursorKey);

        final ResponseList<Family> page = new ResponseList<Family>(items, nextCursorKey);
        page.withTotal(total).withLimit(items.size());

        // populate previous
        if (!"0".equals(cursorKey)) {
            final int previousCursor = Math.abs(Integer.parseInt(cursorKey) - pageSize);
            page.withReverseCursor(String.format("%s", previousCursor));
        }

        return page;
    }
    /**
     * Returns list of countries from dummy database.
     * 
     * @return list of countries
     */
    public List<Family> list() {
        final String sql = "select * from " + Family.TABLE;
        List<Family> list = this.jdbcTemplate.query(sql, new RowMapper<Family>() {

            @Override
            public Family mapRow(ResultSet rs, int paramInt) throws SQLException {
                final Family domain = new Family();
                domain.setId(rs.getLong("id"));
                domain.setFather(Long.parseLong(rs.getString("father")));
                domain.setMother(Long.parseLong(rs.getString("mother")));
                domain.setChild(Long.parseLong(rs.getString("child")));
                domain.setNote(rs.getString("note"));
                domain.setCreatedAt(new Date(rs.getTimestamp("createdAt").getTime()));
                return domain;
            }
            
        });
        return list;
    }
    
    /**
     * @param parentId
     * @param userType
     * @return
     */
    public List<Family> getFamily(Long parentId, Long userType) {
        String sql = "select * from " + Family.TABLE + " WHERE father = ?";
        if (userType == 2) {
        	sql = "select * from " + Family.TABLE + " WHERE mother = ?";
        }
        
        List<Family> list = this.jdbcTemplate.query(sql, new Object[]{ parentId}, new RowMapper<Family>() {
            @Override
            public Family mapRow(ResultSet rs, int paramInt) throws SQLException {
                final Family domain = new Family();
                domain.setId(rs.getLong("id"));
                domain.setFather(Long.parseLong(rs.getString("father")));
                domain.setMother(Long.parseLong(rs.getString("mother")));
                domain.setChild(Long.parseLong(rs.getString("child")));
                domain.setNote(rs.getString("note"));
                domain.setCreatedAt(new Date(rs.getTimestamp("createdAt").getTime()));
                return domain;
            }
        });
        return list;
    }

    /**
     * @param id Family id
     * @return Family object for given id
     */
    public Family get(Long id) {
        final String sql = "select * from " + Family.TABLE + " where id = ?";
        
        try {
            //select for object
            final Family obj = jdbcTemplate.queryForObject(sql, new Object[] { id }, new RowMapper<Family>() {

                @Override
                public Family mapRow(ResultSet rs, int paramInt) throws SQLException {
                    final Family domain = new Family();
                    domain.setId(rs.getLong("id"));
                    domain.setFather(Long.parseLong(rs.getString("father")));
                    domain.setMother(Long.parseLong(rs.getString("mother")));
                    domain.setChild(Long.parseLong(rs.getString("child")));
                    domain.setNote(rs.getString("note"));
                    domain.setCreatedAt(new Date(rs.getTimestamp("createdAt").getTime()));
                    return domain;
                }
            });
            return obj;
        } catch (EmptyResultDataAccessException e) {
            System.out.println("NOT FOUND " + id + " in Table" );
            return null;
        }
    }

    /**
     * Create new Family in dummy database. Updates the id and insert new Family in list.
     * 
     * @param Family Family object
     * @return Family object with updated id
     */
    public Family create(Family family) {
        final String sql = "INSERT INTO " + Family.TABLE + " (father,mother,child,note) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql, new Object[] { family.getFather(), family.getMother(), family.getChild(), family.getNote() });
        
        Family obj = jdbcTemplate.queryForObject("SELECT * from " + Family.TABLE + " ORDER BY id DESC LIMIT 1", new RowMapper<Family>() {
            @Override
            public Family mapRow(ResultSet rs, int paramInt) throws SQLException {
                final Family domain = new Family();
                domain.setId(rs.getLong("id"));
                domain.setFather(Long.parseLong(rs.getString("father")));
                domain.setMother(Long.parseLong(rs.getString("mother")));
                domain.setChild(Long.parseLong(rs.getString("child")));
                domain.setNote(rs.getString("note"));
                domain.setCreatedAt(new Date(rs.getTimestamp("createdAt").getTime()));
                return domain;
            }
        });
        family = obj;
        return family;
    }

    /**
     * @param id the Family id
     * @return id of deleted Family object
     */
    public Long delete(Long id) {
        final String sql = "Delete from " + Family.TABLE + " where id =?";
        int result = jdbcTemplate.update(sql, new Object[] { id });
        return result == 1 ? id : null;
    }
    
    /**
     * @param parentId
     * @param childId
     * @param userType
     * @return
     */
    public boolean deleteChild(Long parentId, Long childId, Long userType) {
    	String sql = "Delete from " + Family.TABLE + " where father = ? AND child =? ";
    	if (userType == 2) {
        	sql = "select * from " + Family.TABLE + " WHERE mother = ? AND child =? ";
        }
        int result = jdbcTemplate.update(sql, new Object[] { parentId, childId });
        return result == 1 ? true : false;
    }

    /**
     * Update the Family object for given id in dummy database. If Family not exists, returns null
     * 
     * @param Family
     * @return Family object with id
     */
    public Family update(Family family) {
        final String sql = "UPDATE " + Family.TABLE + " set father =? , mother =?, child =?, note =? where id=?";
        int result = jdbcTemplate.update(sql, new Object[] { family.getFather(), family.getMother(), family.getChild(), family.getNote() , family.getId()});
        return result == 1 ? family : null;

    }
    
    /**
     * @param father
     * @param mother
     * @param isFather
     */
    public void updateFamily(Long father, Long mother, boolean isFather) {
        if (isFather) {
        	final String sql = "UPDATE " + Family.TABLE + " set mother =? where father=?";
        	int result = jdbcTemplate.update(sql, new Object[] { mother, father});
        } else {
        	final String sql = "UPDATE " + Family.TABLE + " set father =? where mother=?";
        	int result = jdbcTemplate.update(sql, new Object[] { father, mother});
        }
    }

}
