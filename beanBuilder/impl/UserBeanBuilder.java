package org.cbb.dba.beanBuilder.impl;

import org.cbb.bean.Passage;
import org.cbb.bean.User;
import org.cbb.dba.beanBuilder.BeanBuilder;
import org.cbb.dba.newJdbc.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Colossus on 2018/1/10.
 */
public class UserBeanBuilder implements BeanBuilder<User> {
    @Override
    public User setBean(ResultSet rs) throws SQLException {
        User user=new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        return user;
    }

    @Override
    public User setBean(ResultSet rs, JdbcTemplate template) throws SQLException {
    	User user = new User();
    	user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        PassageBeanBuilder psb = new PassageBeanBuilder();
        String sql = "select * from passage";
        HashMap<String,Integer> map = new HashMap<String,Integer>();
        map.put("uid", user.getId());
        map.put("ispass",1);
        List ps = template.selectList(sql, map, psb,false);
        Set<Passage> passages;
        if(ps==null) passages=null;
        else passages=new HashSet<>(ps);
        user.setPassages(passages);
        return user;
    }
}
