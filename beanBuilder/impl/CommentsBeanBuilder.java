package org.cbb.dba.beanBuilder.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.cbb.bean.Comments;
import org.cbb.bean.User;
import org.cbb.dba.beanBuilder.BeanBuilder;
import org.cbb.dba.newJdbc.JdbcTemplate;
import org.cbb.dba.newJdbc.SafeResult;

public class CommentsBeanBuilder implements BeanBuilder<Comments> {

	@Override
	public Comments setBean(ResultSet rs) throws SQLException {
		Comments com = new Comments();
		com.setId(rs.getInt("id"));
		com.setPid(rs.getInt("pid"));
		com.setLikeNum(rs.getInt("likeNum"));
		com.setContent(rs.getString("content"));
		com.setUid(rs.getInt("uid"));
		
//		HashMap<String,Integer> temp = new HashMap<String,Integer>();
//		temp.put("id", rs.getInt("uid"));
//		ResultSet userown = template.select("select * from user",temp);
//		if(userown.next())
//		{
//			User tempuser = new User();
//			tempuser.setId(userown.getInt("id"));
//			tempuser.setUsername(userown.getString("username"));
//			com.setUser(tempuser);
//		}
		return com;
	}

	@Override
	public Comments setBean(ResultSet rs, JdbcTemplate template) throws SQLException {
		Comments com = new Comments();
		com.setId(rs.getInt("id"));
		com.setPid(rs.getInt("pid"));
		com.setLikeNum(rs.getInt("likeNum"));
		com.setContent(rs.getString("content"));
		com.setUid(rs.getInt("uid"));
		
		HashMap<String,Integer> temp = new HashMap<String,Integer>();
		temp.put("id", rs.getInt("uid"));
		SafeResult s1 = template.select("select * from user",temp);
		ResultSet userown = s1.getResultSet();
		if(userown.next())
		{
			User tempuser = new User();
			tempuser.setId(userown.getInt("id"));
			tempuser.setUsername(userown.getString("username"));
			com.setUser(tempuser);
		}
		userown.close();
		
		HashMap<String,Integer> temp1 = new HashMap<String,Integer>();
		temp1.put("cid", com.getId());
		SafeResult s2 = template.select("select * from palikerelation",temp1);
		ResultSet comliker = s2.getResultSet();
		List likeusers = null;
		while(comliker.next())
		{
			HashMap<String,Integer> temp2 = new HashMap<String,Integer>();
			temp2.put("id", comliker.getInt("uid"));
			likeusers.add(template.selectList("select * from user", temp, new UserBeanBuilder(),false).get(0));
		}
		comliker.close();
		Set<User> userset = new HashSet(likeusers);
		com.setLikeUsers(userset);
		
		s1.close();
		s2.close();
		return com;
	}

}
