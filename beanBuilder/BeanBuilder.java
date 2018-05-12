package org.cbb.dba.beanBuilder;



import java.sql.ResultSet;
import java.sql.SQLException;

import org.cbb.dba.newJdbc.JdbcTemplate;

/**
 * Created by Colossus on 2018/1/10.
 */
public interface BeanBuilder<E> {
    public E setBean(ResultSet rs)throws SQLException;
    public E setBean(ResultSet rs, JdbcTemplate template) throws SQLException;
}
