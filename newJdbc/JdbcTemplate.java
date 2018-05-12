package org.cbb.dba.newJdbc;

import org.cbb.dba.beanBuilder.BeanBuilder;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Colossus on 2018/1/11.
 */
public interface JdbcTemplate {
    /*插入一行
       tableName:表名
       parama.key:列名
       parama.value:列的相应值*/
    public void insertRow(String tableName, HashMap parama);

    /*取出表中数据,返回一个线性表
       selectSql:静态搜索语句
       beanBuilder:依据表装配Javabean的接口*/
    public List selectList(String sql, BeanBuilder beanBuilder,boolean isRelated);

    /*取出表中数据，返回一个线性表
       selectSql:搜索语句，不包含约束条件
       constraints.key:约束的列名
       constraints.value:约束列的相应值
       beanBuilder:依据表装配Javabean的接口*/
    public List selectList(String selectSql,HashMap constraints,BeanBuilder beanBuilder,boolean isRelated);

    /*更新数据库中所有记录
        tableName:表名
        parama.key:列名
        parama.value:列的相应值*/
    public void updateRows(String tableName,HashMap parama);

    /*更新数据库
       tableName:表名
       parama.key:列名
       parama.value:列的相应值
       constraints.key:约束的列名
       constraints.value:约束列的相应值*/
    public void updateRows(String tableName,HashMap parama,HashMap constraints);

    /*删除表中的所有记录
       tableName:表名*/
    public void deleteRows(String tableName);

    /*删除数据库的一些记录,返回被删除的记录
       tableName:表名
       constraints.key:约束的列名
       constraints.value:约束列的相应值*/
    public void deleteRows(String tableName,HashMap constraints);

    /*取出表中数据，返回一个SafeResult，可供关闭连接
        selectSql:搜索语句，包含列与表名
        constraints.key:约束的列名
        constraints.value:约束列的相应值*/
    public SafeResult select(String selectSql, HashMap constraints);

    /*取出表中数据，返回一个SafeResult，可供关闭连接
        selectSql:搜索语句，包含列与表名*/
    public SafeResult select(String selectSql);
}
