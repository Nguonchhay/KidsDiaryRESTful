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

import com.rupp.spring.domain.User;
import com.rupp.spring.utility.PasswordUtility;
import com.rupp.spring.domain.ResponseList;

@Repository("userDaoImpl")
public class UserDaoImpl implements UserDao {
    private static final Logger LOG = LoggerFactory.getLogger(UserDaoImpl.class);
    
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public UserDaoImpl(DataSource dataSource) {
        //jdbcTemplate = new JdbcTemplate(DBCP2DataSourceUtils.getDataSource());
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public int count() {
        final String sql = "select count(*) from " + User.TABLE + " WHERE deletedAt IS NULL";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public ResponseList<User> getPage(int pagesize, String offset) {
        if (offset == null) {
            offset = "0";
        }
        final String sql = "select * from " + User.TABLE + " WHERE deletedAt IS NULL limit " + pagesize + " OFFSET " + offset;
        List<User> list = this.jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int paramInt) throws SQLException {
                final User domain = new User();
                domain.setId(rs.getLong("id"));
                domain.setUsername(rs.getString("username"));
                domain.setEncryptPassword(rs.getString("password"));               
                domain.setAccessToken(rs.getString("accessToken"));
                if (rs.getTimestamp("loggedinDate") != null) {
                	domain.setLoggedinDate(new Date(rs.getTimestamp("loggedinDate").getTime()));
                }
                domain.setEmail(rs.getString("email"));
                domain.setPhone(rs.getString("phone"));
                domain.setFirstName(rs.getString("firstName"));
                domain.setLastName(rs.getString("lastName"));
                domain.setSex(rs.getString("sex"));
                domain.setBirthDate(new Date(rs.getTimestamp("birthDate").getTime()));
                domain.setCountry(Integer.parseInt(rs.getString("country")));
                domain.setUserType(Integer.parseInt(rs.getString("userType")));
                domain.setActivated(Boolean.parseBoolean(rs.getString("isActivated")));                
                domain.setCreatedAt(new Date(rs.getTimestamp("createdAt").getTime()));
                return domain;
            }
            
        });
        
        return populatePages(list, pagesize, String.valueOf(offset));
    }
    
    protected ResponseList<User> populatePages(final Collection<User> items, final int pageSize, final String cursorKey) {
        return populatePages(items, pageSize, cursorKey, null);
    }

    protected ResponseList<User> populatePages(final Collection<User> items, final int pageSize, final String cursorKey, final Integer totalCount) {

        if (items == null || items.isEmpty()) {
            return new ResponseList<User>(items);
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

        final ResponseList<User> page = new ResponseList<User>(items, nextCursorKey);
        page.withTotal(total).withLimit(items.size());

        // populate previous
        if (!"0".equals(cursorKey)) {
            final int previousCursor = Math.abs(Integer.parseInt(cursorKey) - pageSize);
            page.withReverseCursor(String.format("%s", previousCursor));
        }

        return page;
    }
    /**
     * Returns list of users from dummy database.
     * 
     * @return list of users
     */
    public List<User> list() {
        final String sql = "select * from " + User.TABLE;
        List<User> list = this.jdbcTemplate.query(sql, new RowMapper<User>() {

            @Override
            public User mapRow(ResultSet rs, int paramInt) throws SQLException {
                final User domain = new User();
                domain.setId(rs.getLong("id"));
                domain.setUsername(rs.getString("username"));
                domain.setEncryptPassword(rs.getString("password"));               
                domain.setAccessToken(rs.getString("accessToken"));
                domain.setLoggedinDate(new Date(rs.getTimestamp("loggedinDate").getTime()));
                domain.setEmail(rs.getString("email"));
                domain.setPhone(rs.getString("phone"));
                domain.setFirstName(rs.getString("firstName"));
                domain.setLastName(rs.getString("lastName"));
                domain.setSex(rs.getString("sex"));
                domain.setBirthDate(new Date(rs.getTimestamp("birthDate").getTime()));
                domain.setCountry(Integer.parseInt(rs.getString("country")));
                domain.setUserType(Integer.parseInt(rs.getString("userType")));
                domain.setActivated(Boolean.parseBoolean(rs.getString("isActivated")));                
                domain.setCreatedAt(new Date(rs.getTimestamp("createdAt").getTime()));
                return domain;
            }
            
        });
        return list;
    }
    
    /**
     * @param username
     * @param password
     * @return UserType object for given id
     */
    public User login(String username, String password) {
        final String sql = "select * from " + User.TABLE + " where username = ? and password = ?";
        String convertPassword = PasswordUtility.encrypt(password);
        try {
            //select for object
            final User obj = jdbcTemplate.queryForObject(sql, new Object[] { username, convertPassword }, new RowMapper<User>() {

                @Override
                public User mapRow(ResultSet rs, int paramInt) throws SQLException {
                    final User domain = new User();
                    domain.setId(rs.getLong("id"));
                    domain.setUsername(rs.getString("username"));
                    domain.setEncryptPassword(rs.getString("password"));               
                    domain.setAccessToken(rs.getString("accessToken"));
                    if (rs.getTimestamp("loggedinDate") != null) {
                    	domain.setLoggedinDate(new Date(rs.getTimestamp("loggedinDate").getTime()));
                    }
                    domain.setEmail(rs.getString("email"));
                    domain.setPhone(rs.getString("phone"));
                    domain.setFirstName(rs.getString("firstName"));
                    domain.setLastName(rs.getString("lastName"));
                    domain.setSex(rs.getString("sex"));
                    domain.setBirthDate(new Date(rs.getTimestamp("birthDate").getTime()));
                    domain.setCountry(Integer.parseInt(rs.getString("country")));
                    domain.setUserType(Integer.parseInt(rs.getString("userType")));
                    domain.setActivated(Boolean.parseBoolean(rs.getString("isActivated")));                
                    domain.setCreatedAt(new Date(rs.getTimestamp("createdAt").getTime()));
                    return domain;
                }
            });
            return obj;
        } catch (EmptyResultDataAccessException e) {
            System.out.println("Username or password is incorrect!" );
            return null;
        }
    }


    /**
     * @param id UserType id
     * @return UserType object for given id
     */
    public User get(Long id) {
        final String sql = "select * from " + User.TABLE + " where id = ?";
        
        try {
            //select for object
            final User obj = jdbcTemplate.queryForObject(sql, new Object[] { id }, new RowMapper<User>() {

                @Override
                public User mapRow(ResultSet rs, int paramInt) throws SQLException {
                    final User domain = new User();
                    domain.setId(rs.getLong("id"));
                    domain.setUsername(rs.getString("username"));
                    domain.setEncryptPassword(rs.getString("password"));               
                    domain.setAccessToken(rs.getString("accessToken"));
                    if (rs.getTimestamp("loggedinDate") != null) {
                    	domain.setLoggedinDate(new Date(rs.getTimestamp("loggedinDate").getTime()));
                    }
                    domain.setEmail(rs.getString("email"));
                    domain.setPhone(rs.getString("phone"));
                    domain.setFirstName(rs.getString("firstName"));
                    domain.setLastName(rs.getString("lastName"));
                    domain.setSex(rs.getString("sex"));
                    domain.setBirthDate(new Date(rs.getTimestamp("birthDate").getTime()));
                    domain.setCountry(Integer.parseInt(rs.getString("country")));
                    domain.setUserType(Integer.parseInt(rs.getString("userType")));
                    domain.setActivated(Boolean.parseBoolean(rs.getString("isActivated")));                
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
     * Create new User in dummy database. Updates the id and insert new User in list.
     * 
     * @param User User object
     * @return User object with updated id
     */
    public User create(User user) {
    	user.setAccessToken(String.valueOf((int)(Math.random()*9000) + 1000));
        final String sql = "INSERT INTO " + User.TABLE + " (username,password,email,phone,firstName,lastName,sex,birthDate,country,userType,accessToken,isActivated) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, new Object[] { user.getUsername(), user.getPassword(), user.getEmail(), user.getPhone(), user.getFirstName(), user.getLastName(), user.getSex(), user.getBirthDate(), user.getCountry(), user.getUserType(), user.getAccessToken(), user.isActivated() });
        
        User obj = jdbcTemplate.queryForObject("SELECT * from " + User.TABLE + " ORDER BY id DESC LIMIT 1", new RowMapper<User>() {

            @Override
            public User mapRow(ResultSet rs, int paramInt) throws SQLException {
                final User domain = new User();
                domain.setId(rs.getLong("id"));
                domain.setUsername(rs.getString("username"));
                domain.setEncryptPassword(rs.getString("password"));               
                domain.setAccessToken(rs.getString("accessToken"));
                if (rs.getTimestamp("loggedinDate") != null) {
                	domain.setLoggedinDate(new Date(rs.getTimestamp("loggedinDate").getTime()));
                }
                domain.setEmail(rs.getString("email"));
                domain.setPhone(rs.getString("phone"));
                domain.setFirstName(rs.getString("firstName"));
                domain.setLastName(rs.getString("lastName"));
                domain.setSex(rs.getString("sex"));
                domain.setBirthDate(new Date(rs.getTimestamp("birthDate").getTime()));
                domain.setCountry(Integer.parseInt(rs.getString("country")));
                domain.setUserType(Integer.parseInt(rs.getString("userType")));
                domain.setActivated(Boolean.parseBoolean(rs.getString("isActivated")));                
                domain.setCreatedAt(new Date(rs.getTimestamp("createdAt").getTime()));
                return domain;
            }
        });        
        user = obj;

        return user;
    }

    /**
     * @param id the User id
     * @return id of deleted User object
     */
    public Long delete(Long id) {
        final String sql = "Delete from " + User.TABLE + " where id =?";
        int result = jdbcTemplate.update(sql, new Object[] { id });
        return result == 1 ? id : null;
    }

    /**
     * Update the User object for given id in dummy database. If User not exists, returns null
     * 
     * @param User
     * @return User object with id
     */
    public User update(User user) {
    	String password = PasswordUtility.decrypt(user.getPassword());
        final String sql = "UPDATE " + User.TABLE + " set username =? , password =?, accessToken =? , loggedinDate =? , email =? , phone =? , firstName =?, lastName =?, sex =? , birthDate =? , country =? , userType = ? , isActivated =? , deletedAt =? where id=?";
        int result = jdbcTemplate.update(sql, new Object[] { user.getUsername(), password, user.getAccessToken(), user.getLoggedinDate(), user.getEmail(), user.getPhone(), user.getFirstName(), user.getLastName(), user.getSex(), user.getBirthDate(), user.getCountry(), user.getUserType(), user.isActivated(), user.getDeletedAt() , user.getId()});
        return result == 1 ? user : null;

    }

}
