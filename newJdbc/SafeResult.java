package org.cbb.dba.newJdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Colossus on 2018/1/18.
 */
public class SafeResult {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
    public void close() throws SQLException {
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}
