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

import com.rupp.spring.domain.DailyActivity;
import com.rupp.spring.domain.ResponseList;

@Repository("activityDaoImpl")
public class DailyActivityDaoImpl implements DailyActivityDao {
    private static final Logger LOG = LoggerFactory.getLogger(DailyActivityDaoImpl.class);
    
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public DailyActivityDaoImpl(DataSource dataSource) {
        //jdbcTemplate = new JdbcTemplate(DBCP2DataSourceUtils.getDataSource());
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public int count() {
        final String sql = "select count(*) from " + DailyActivity.TABLE + " WHERE deletedAt IS NULL";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public ResponseList<DailyActivity> getPage(int pagesize, String offset) {
        if (offset == null) {
            offset = "0";
        }
        final String sql = "select * from " + DailyActivity.TABLE + " WHERE deletedAt IS NULL limit " + pagesize + " OFFSET " + offset;
        List<DailyActivity> list = this.jdbcTemplate.query(sql, new RowMapper<DailyActivity>() {

            @Override
            public DailyActivity mapRow(ResultSet rs, int paramInt) throws SQLException {
                final DailyActivity domain = new DailyActivity();
                domain.setId(rs.getLong("id"));
                domain.setParent(rs.getLong("parent"));
                domain.setChild(rs.getLong("child"));
                domain.setActivityDate(new Date(rs.getTimestamp("activityDate").getTime()));
                domain.setApproved(rs.getBoolean("isActivated"));
                domain.setCreatedAt(new Date(rs.getTimestamp("createdAt").getTime()));
                return domain;
            }
            
        });
        
        return populatePages(list, pagesize, String.valueOf(offset));
    }
    
    protected ResponseList<DailyActivity> populatePages(final Collection<DailyActivity> items, final int pageSize, final String cursorKey) {
        return populatePages(items, pageSize, cursorKey, null);
    }

    protected ResponseList<DailyActivity> populatePages(final Collection<DailyActivity> items, final int pageSize, final String cursorKey, final Integer totalCount) {

        if (items == null || items.isEmpty()) {
            return new ResponseList<DailyActivity>(items);
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

        final ResponseList<DailyActivity> page = new ResponseList<DailyActivity>(items, nextCursorKey);
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
     * @param userType
     * @param child
     * @return list of countries
     */
    public List<DailyActivity> list(Long parentId, int userType, Long child) {
        final String sql = "select * from " + DailyActivity.TABLE + " WHERE deletedAt IS NULL AND parentId=" + parentId;
        List<DailyActivity> list = this.jdbcTemplate.query(sql, new RowMapper<DailyActivity>() {

            @Override
            public DailyActivity mapRow(ResultSet rs, int paramInt) throws SQLException {
                final DailyActivity domain = new DailyActivity();
                domain.setId(rs.getLong("id"));
                domain.setParent(rs.getLong("parent"));
                domain.setChild(rs.getLong("child"));
                domain.setActivityDate(new Date(rs.getTimestamp("activityDate").getTime()));
                domain.setApproved(rs.getBoolean("isActivated"));
                domain.setCreatedAt(new Date(rs.getTimestamp("createdAt").getTime()));
                return domain;
            }
        });
        return list;
    }

    /**
     * @param id DailyActivity id
     * @return DailyActivity object for given id
     */
    public DailyActivity get(Long id) {
        final String sql = "select * from " + DailyActivity.TABLE + " where deletedAt IS NULL AND id = ?";
        
        try {
            //select for object
            final DailyActivity obj = jdbcTemplate.queryForObject(sql, new Object[] { id }, new RowMapper<DailyActivity>() {

                @Override
                public DailyActivity mapRow(ResultSet rs, int paramInt) throws SQLException {
                    final DailyActivity domain = new DailyActivity();
                    domain.setId(rs.getLong("id"));
                    domain.setParent(rs.getLong("parent"));
                    domain.setChild(rs.getLong("child"));
                    domain.setActivityDate(new Date(rs.getTimestamp("activityDate").getTime()));
                    domain.setApproved(rs.getBoolean("isActivated"));
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
    public DailyActivity create(DailyActivity dailyActivity) {
        final String sql = "INSERT INTO " + DailyActivity.TABLE + " (parent,child,activityDate) VALUES (?,?,?)";
        jdbcTemplate.update(sql, new Object[] { dailyActivity.getParent(), dailyActivity.getChild(), dailyActivity.getActivityDate() });
        return dailyActivity;
    }

    /**
     * @param id the DailyActivity id
     * @return id of deleted DailyActivity object
     */
    public Long delete(Long id) {
        final String sql = "Delete from " + DailyActivity.TABLE + " where id =?";
        int result = jdbcTemplate.update(sql, new Object[] { id });
        return result == 1 ? id : null;
    }

    /**
     * Update the Activity object for given id in dummy database. If Activity not exists, returns null
     * 
     * @param Activity
     * @return Activity object with id
     */
    public DailyActivity update(DailyActivity dailyActivity) {
        final String sql = "UPDATE " + DailyActivity.TABLE + " set parent =?, child =? , activityDate =?, isApproved =?, deletedAt =? where id=?";
        int result = jdbcTemplate.update(sql, new Object[] { dailyActivity.getParent(), dailyActivity.getChild(), dailyActivity.getActivityDate(), dailyActivity.isApproved(), dailyActivity.getDeletedAt() , dailyActivity.getId()});
        return result == 1 ? dailyActivity : null;
    }

}
