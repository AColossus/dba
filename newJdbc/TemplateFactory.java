package org.cbb.dba.newJdbc;

/**
 * Created by Colossus on 2018/1/11.
 */
public class TemplateFactory {
    public JdbcTemplate getMysqlJdbcTemplate(){
        return new MysqlJdbcTemplate();
    }
    public JdbcTemplate getMysqlJdbcTemplate(String driverName, String url, String username, String password){
        return new MysqlJdbcTemplate(url,username,password);
    }
    public JdbcTemplate getMysqlJdbcTemplate(String driverName, String url, String username, String password,
                                                  int capacity, int timeout){
        return new MysqlJdbcTemplate(url,username,password,capacity,timeout);
    }
}
