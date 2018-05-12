package org.cbb.dba.newJdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Colossus on 2018/1/11.
 */
public class SqlCreatorImp implements SqlCreator {
    public StringBuffer createInsertSql(String tableName, HashMap parama) {
        StringBuffer sql=new StringBuffer("INSERT INTO "),values=new StringBuffer("(");
        sql.append(tableName+" (");
        Iterator iterator=parama.keySet().iterator();
        while(iterator.hasNext()){
            sql.append(iterator.next().toString());
            if(iterator.hasNext()){
                sql.append(",");
                values.append("?,");
            }
            else{
                sql.append(") ");
                values.append("?)");
            }
        }
        sql.append("VALUES "+values);
        return sql;
    }

    public StringBuffer createSelectSql(String selectSql, HashMap constraints)  {
        StringBuffer sql=new StringBuffer(selectSql);
        if(constraints!=null) {
            sql.append(" WHERE ");
            Iterator<String> iterator = constraints.keySet().iterator();
            while (iterator.hasNext()) {
                sql.append(iterator.next() + "=?");
                if (iterator.hasNext()) sql.append(" AND ");
            }
        }
        return sql;
    }

    public StringBuffer createUpdateSql(String tableName, HashMap parama, HashMap constraints) throws Exception {
        if(parama==null) throw new Exception("No parama!");
        StringBuffer sql=new StringBuffer("UPDATE "+tableName+" SET ");
        Iterator<String> iterator=parama.keySet().iterator();
        while(iterator.hasNext()){
            sql.append(iterator.next()+"=? ");
            if(iterator.hasNext()) sql.append(", ");
        }
        if(constraints!=null){
            sql.append("WHERE ");
            iterator=constraints.keySet().iterator();
            while(iterator.hasNext()){
                sql.append(iterator.next()+"=? ");
                if(iterator.hasNext()) sql.append("AND ");
            }
        }
        return sql;
    }

    public StringBuffer createDeleteSql(String tableName, HashMap parama) {
        StringBuffer sql=new StringBuffer("DELETE FROM ");
        sql.append(tableName);
        if(parama!=null){
            sql.append(" WHERE ");
            Iterator<String> iterator=parama.keySet().iterator();
            while (iterator.hasNext()){
                sql.append(iterator.next()+"=? ");
                if(iterator.hasNext())sql.append("AND ");
            }
        }
        return  sql;
    }

    public int matchParama(PreparedStatement ps, HashMap parama) throws ClassFormatError, SQLException {
        int counts=matchParama(ps,parama,0);
        return counts;
    }

    public int matchParama(PreparedStatement ps, HashMap parama, int start) throws ClassFormatError, SQLException {
        Iterator iterator=parama.values().iterator();
        int index=start;
        while(iterator.hasNext()){
            index++;
            Object value=iterator.next();
            String className=value.getClass().getName();
            if(className.equals("java.lang.String"))
                ps.setString(index, (String) value);
            else if(className.equals("java.lang.Integer"))
                ps.setInt(index, (Integer) value);
            else if(className.equals("java.lang.Float"))
                ps.setFloat(index,(Float)value);
            else if(className.equals("java.lang.Double"))
                ps.setDouble(index,(Double)value);
            else if(className.equals("java.lang.Boolean"))
                ps.setBoolean(index,(Boolean)value);
            else
                throw new ClassFormatError("Can't match format");
        }
        return index+1;
    }
}
