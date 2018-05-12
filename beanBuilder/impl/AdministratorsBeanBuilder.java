package org.cbb.dba.beanBuilder.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.cbb.bean.Administrators;
import org.cbb.bean.Passage;
import org.cbb.bean.User;
import org.cbb.dba.beanBuilder.BeanBuilder;
import org.cbb.dba.newJdbc.JdbcTemplate;

public class AdministratorsBeanBuilder implements BeanBuilder<Administrators> {

	@Override
	public Administrators setBean(ResultSet rs) throws SQLException {
		Administrators admin = new Administrators();
        admin.setId(rs.getInt("id"));
        admin.setUsername(rs.getString("username"));
        admin.setPassword(rs.getString("password"));
        return admin;
	}

	@Override
	public Administrators setBean(ResultSet rs, JdbcTemplate template) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
