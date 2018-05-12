package org.cbb.dba.beanBuilder.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.cbb.bean.Comments;
import org.cbb.bean.Passage;
import org.cbb.bean.User;
import org.cbb.dba.beanBuilder.BeanBuilder;
import org.cbb.dba.newJdbc.JdbcTemplate;
import org.cbb.dba.newJdbc.SafeResult;

public class PassageBeanBuilder implements BeanBuilder<Passage> {
	@Override
	public Passage setBean(ResultSet rs) throws SQLException {
		Passage ps = new Passage();
		ps.setId(rs.getInt("id"));
		ps.setUid(rs.getInt("uid"));
		ps.setLikeNum(rs.getInt("likeNum"));
		ps.setRank(rs.getInt("rank"));
		ps.setIspass(rs.getBoolean("ispass"));
		ps.setTitle(rs.getString("title"));
		ps.setContent(rs.getString("content"));
		ps.setComNum(rs.getInt("comNum"));
		return ps;
	}

	@Override
	public Passage setBean(ResultSet rs, JdbcTemplate template) throws SQLException {
		Passage ps = new Passage();
		ps.setId(rs.getInt("id"));
		ps.setUid(rs.getInt("uid"));
		ps.setLikeNum(rs.getInt("likeNum"));
		ps.setRank(rs.getInt("rank"));
		ps.setIspass(rs.getBoolean("ispass"));
		ps.setTitle(rs.getString("title"));
		ps.setContent(rs.getString("content"));
		ps.setComNum(rs.getInt("comNum"));
		String sql = "select * from comments";
		
        HashMap<String,Integer> map = new HashMap<String,Integer>();        
        map.put("pid", ps.getId());
//		ResultSet comrs = template.select(sql, map);
		List com = template.selectList(sql, map, new CommentsBeanBuilder(),false);
		Set<Comments> comset ;
		if(com!=null)  comset = new HashSet(com);
		else  comset=null;
		ps.setComments(comset);
		
//		SafeResult s1 = template.select("select * from palikerelation",map);
//		ResultSet paliker = s1.getResultSet();
//		List likeusers = null;
//		while(paliker.next())
//		{
//			HashMap<String,Integer> temp = new HashMap<String,Integer>();
//			temp.put("id", paliker.getInt("uid"));
//			likeusers.add(template.selectList("select * from user", temp, new UserBeanBuilder(),false).get(0));
//		}
//		paliker.close();
//		Set<User> userset = new HashSet(likeusers);
//		ps.setLikeUsers(userset);
		
		HashMap<String,Integer> temp = new HashMap<String,Integer>();
		temp.put("id", ps.getUid());
		SafeResult s2 = template.select("select * from user",temp);
		ResultSet userown = s2.getResultSet();
		if(userown.next())
		{
			User tempuser = new User();
			tempuser.setId(userown.getInt("id"));
			tempuser.setUsername(userown.getString("username"));
			ps.setUser(tempuser);
		}
		userown.close();
//		s1.close();
		s2.close();
		return ps;
	}

}
