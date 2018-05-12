package org.cbb.dba.newJdbc;


import org.cbb.dba.beanBuilder.BeanBuilder;
import org.cbb.dba.dataSource.DatabasePool;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Colossus on 2018/1/9.
 */
public class MysqlJdbcTemplate implements JdbcTemplate {
    private DatabasePool databasePool;
    private SqlCreator sqlCreator=new SqlCreatorImp();
    Connection connection;
    public MysqlJdbcTemplate(){
        String driverName="com.mysql.jdbc.Driver";
        String url="jdbc:mysql://localhost:3306/blogdata?characterEncoding=utf-8";
        String username="root";
        String password="Still_Sleep0";
        databasePool =DatabasePool.getDatabasePool(driverName,url,username,password,10,60*60);
    }
    public MysqlJdbcTemplate( String url, String username, String password){
        String driverName="com.mysql.jdbc.Driver";
        databasePool =DatabasePool.getDatabasePool(driverName,url,username,password,10,60*60);
    }

    public MysqlJdbcTemplate(String url, String username, String password,
                             int capacity, int timeout){
        String driverName="com.mysql.jdbc.Driver";
        databasePool =DatabasePool.getDatabasePool(driverName,url,username,password,capacity,timeout);
    }
    public void insertRow(String tableName, HashMap parama){
        try {
            connection= databasePool.getConnection();
            StringBuffer sql=sqlCreator.createInsertSql(tableName,parama);
            PreparedStatement ps=connection.prepareStatement(sql.toString());
            sqlCreator.matchParama(ps,parama);
            ps.executeUpdate();
        }
        catch(SQLException se) {
            se.printStackTrace();
        }
        finally {
            if(connection!=null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    public List selectList(String sql,BeanBuilder beanBuilder,boolean isRelated){
        return selectList(sql,null,beanBuilder,isRelated);
    }

    public List selectList(String selectSql,HashMap constraints,BeanBuilder beanBuilder,boolean isRelated){
        List resultList=new ArrayList();
        try {
            connection=databasePool.getConnection();
            StringBuffer sql=sqlCreator.createSelectSql(selectSql,constraints);
            PreparedStatement ps=connection.prepareStatement(sql.toString());
            if(constraints!=null)
                sqlCreator.matchParama(ps,constraints);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                if(!isRelated) resultList.add(beanBuilder.setBean(rs));
                else resultList.add(beanBuilder.setBean(rs,this));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if(resultList.size()!=0) return resultList;
        else return null;
    }

    public void updateRows(String tableName,HashMap parma){
        updateRows(tableName,parma,null);
    }

    public void updateRows(String tableName,HashMap parama,HashMap constranints){
        try {
            connection=databasePool.getConnection();
            StringBuffer sql=sqlCreator.createUpdateSql(tableName,parama,constranints);
            PreparedStatement ps=connection.prepareStatement(sql.toString());
            int index=sqlCreator.matchParama(ps,parama);
            if(constranints!=null)
                sqlCreator.matchParama(ps,constranints,index-1);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deleteRows(String tableName) {
        deleteRows(tableName,null);
    }

    public void deleteRows(String tableName, HashMap constraints) {
        try {
            connection=databasePool.getConnection();
            StringBuffer sql=sqlCreator.createDeleteSql(tableName,constraints);
            PreparedStatement ps=connection.prepareStatement(sql.toString());
            if(constraints!=null)
                sqlCreator.matchParama(ps,constraints);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public SafeResult select(String selectSql){
        return  select(selectSql,null);
    }

    public SafeResult select(String selectSql, HashMap constraints){
        try {
            SafeResult safeResult=new SafeResult();
            connection=databasePool.getConnection();
            StringBuffer sql=sqlCreator.createSelectSql(selectSql,constraints);
            PreparedStatement ps=connection.prepareStatement(sql.toString());
            if(constraints!=null)
                sqlCreator.matchParama(ps,constraints);
            ResultSet rs=ps.executeQuery();
            safeResult.setConnection(connection);
            safeResult.setPreparedStatement(ps);
            safeResult.setResultSet(rs);
            return safeResult;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
