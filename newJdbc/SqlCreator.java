package org.cbb.dba.newJdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Colossus on 2018/1/11.
 */
public interface SqlCreator {
    /*返回未匹配的插入语句
       tableName:表名
       parama.key:列名
       parama.value:列的相应值*/
    public StringBuffer createInsertSql(String tableName, HashMap parama);

    /*返回未匹配的搜索语句
       selectSql:搜索语句，包含列与表名
       constraints.key:约束的列名
       constraints.value:约束列的相应值*/
    public StringBuffer createSelectSql(String selectSql,HashMap constraints)throws Exception;

    /*返回未匹配的更新语句
       tableName:表名
       parama.key:列名
       parama.value:列的相应值
       constraints.key:约束的列名
       constraints.value:约束列的相应值 */
    public StringBuffer createUpdateSql(String tableName,HashMap parama,HashMap constraints)throws Exception;

    /*返回未匹配的删除语句
       tableName:表名
       constraints.key:约束的列名
       constraints.value:约束列的相应值 */
    public StringBuffer createDeleteSql(String tableName,HashMap constraints);

    /*匹配参数，返回参数个数
        ps:待匹配的PreparedStatement
        parama.key:参数名
        parama.value:参数值 */
    public int matchParama(PreparedStatement ps, HashMap parama) throws ClassFormatError, SQLException;

    /*匹配参数
        ps:待匹配的PreparedStatement
        parama.key:参数名
        parama.value:参数值
        start:匹配开始的位置 */
    public int matchParama(PreparedStatement ps,HashMap parama,int start) throws ClassFormatError, SQLException;

}
