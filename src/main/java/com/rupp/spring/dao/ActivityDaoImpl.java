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

import com.rupp.spring.domain.Activity;
import com.rupp.spring.domain.ResponseList;

@Repository("activityDaoImpl")
public class ActivityDaoImpl implements ActivityDao {
    private static final Logger LOG = LoggerFactory.getLogger(ActivityDaoImpl.class);
    
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public ActivityDaoImpl(DataSource dataSource) {
        //jdbcTemplate = new JdbcTemplate(DBCP2DataSourceUtils.getDataSource());
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public int count() {
        final String sql = "select count(*) from " + Activity.TABLE + " WHERE deletedAt IS NULL";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public ResponseList<Activity> getPage(int pagesize, String offset) {
        if (offset == null) {
            offset = "0";
        }
        final String sql = "select * from " + Activity.TABLE + " WHERE deletedAt IS NULL limit " + pagesize + " OFFSET " + offset;
        List<Activity> list = this.jdbcTemplate.query(sql, new RowMapper<Activity>() {

            @Override
            public Activity mapRow(ResultSet rs, int paramInt) throws SQLException {
                final Activity domain = new Activity();
                domain.setId(rs.getLong("id"));
                domain.setParentId(rs.getLong("parentId"));
                domain.setName(rs.getString("name"));
                domain.setIcon(rs.getString("icon"));
                domain.setScore(rs.getInt("score"));
                domain.setNote(rs.getString("note"));
                domain.setActivated(rs.getBoolean("isActivated"));
                domain.setCreatedAt(new Date(rs.getTimestamp("createdAt").getTime()));
                return domain;
            }
            
        });
        
        return populatePages(list, pagesize, String.valueOf(offset));
    }
    
    protected ResponseList<Activity> populatePages(final Collection<Activity> items, final int pageSize, final String cursorKey) {
        return populatePages(items, pageSize, cursorKey, null);
    }

    protected ResponseList<Activity> populatePages(final Collection<Activity> items, final int pageSize, final String cursorKey, final Integer totalCount) {

        if (items == null || items.isEmpty()) {
            return new ResponseList<Activity>(items);
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

        final ResponseList<Activity> page = new ResponseList<Activity>(items, nextCursorKey);
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
     * @param parentId
     * @return list of countries
     */
    public List<Activity> list(Long parentId) {
        final String sql = "select * from " + Activity.TABLE + " WHERE deletedAt IS NULL AND parentId=" + parentId;
        List<Activity> list = this.jdbcTemplate.query(sql, new RowMapper<Activity>() {

            @Override
            public Activity mapRow(ResultSet rs, int paramInt) throws SQLException {
                final Activity domain = new Activity();
                domain.setId(rs.getLong("id"));
                domain.setParentId(rs.getLong("parentId"));
                domain.setName(rs.getString("name"));
                domain.setIcon(rs.getString("icon"));
                domain.setScore(rs.getInt("score"));
                domain.setNote(rs.getString("note"));
                domain.setActivated(rs.getBoolean("isActivated"));
                domain.setCreatedAt(new Date(rs.getTimestamp("createdAt").getTime()));
                return domain;
            }
        });
        return list;
    }

    /**
     * @param id Activity id
     * @return Activity object for given id
     */
    public Activity get(Long id) {
        final String sql = "select * from " + Activity.TABLE + " where deletedAt IS NULL AND id = ?";
        
        try {
            //select for object
            final Activity obj = jdbcTemplate.queryForObject(sql, new Object[] { id }, new RowMapper<Activity>() {

                @Override
                public Activity mapRow(ResultSet rs, int paramInt) throws SQLException {
                    final Activity domain = new Activity();
                    domain.setId(rs.getLong("id"));
                    domain.setParentId(rs.getLong("parentId"));
                    domain.setName(rs.getString("name"));
                    domain.setIcon(rs.getString("icon"));
                    domain.setScore(rs.getInt("score"));
                    domain.setNote(rs.getString("note"));
                    domain.setActivated(rs.getBoolean("isActivated"));
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
     * Create new Activity in dummy database. Updates the id and insert new Activity in list.
     * 
     * @param Activity Activity object
     * @return Activity object with updated id
     */
    public Activity create(Activity activity) {
        final String sql = "INSERT INTO " + Activity.TABLE + " (parentId,name,icon,score,note) VALUES (?,?,?,?,?)";
        jdbcTemplate.update(sql, new Object[] { activity.getParentId(), activity.getName(), activity.getIcon(), activity.getScore(), activity.getNote() });
        return activity;
    }

    /**
     * @param id the Activity id
     * @return id of deleted Activity object
     */
    public Long delete(Long id) {
        final String sql = "Delete from " + Activity.TABLE + " where id =?";
        int result = jdbcTemplate.update(sql, new Object[] { id });
        return result == 1 ? id : null;
    }

    /**
     * Update the Activity object for given id in dummy database. If Activity not exists, returns null
     * 
     * @param Activity
     * @return Activity object with id
     */
    public Activity update(Activity activity) {
        final String sql = "UPDATE " + Activity.TABLE + " set parentId =?, name =? , icon =?, score =?, note =?, isActivated =?, deletedAt =? where id=?";
        int result = jdbcTemplate.update(sql, new Object[] { activity.getParentId(), activity.getName(), activity.getIcon(), activity.getScore(), activity.getNote(), activity.isActivated(), activity.getDeletedAt() , activity.getId()});
        return result == 1 ? activity : null;
    }

}
