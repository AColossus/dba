package org.cbb.dba.dataSource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Colossus on 2018/1/10.
 */
public class DatabasePool {
    private ConnectionStack connectionStack;
    private final String driverName,url,username,password;
    private volatile boolean isRun=true;
    //单例模式
    private static DatabasePool Singleton;

    private DatabasePool(String driverName, String url, String username, String password,
                        int capacity, int timeout) {
        this.driverName =driverName;
        this.url = url;
        this.username = username;
        this.password = password;
        connectionStack=new ConnectionStack(capacity,timeout);
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized static DatabasePool getDatabasePool(String driverName, String url, String username, String password,
                                         int capacity, int timeout){
        if(Singleton!=null) {
            if(Singleton.getConnectionStack().getTimeout()!=timeout||
                    Singleton.getConnectionStack().getCapacity()!=capacity)
                System.out.println("警告：连接池是单例,第一次初始化后属性值无法更改!");
            return Singleton;
        }
        else
            return Singleton=new DatabasePool(driverName,url,username,password,capacity,timeout);
    }
    public Connection getConnection () throws SQLException {
        if(!isRun) throw new SQLException("pool closed");
        Connection conn=connectionStack.pop();
        if(conn==null) {
            conn = DriverManager.getConnection(url, username, password);
        }
        final Connection myConn=conn;
        return (Connection) Proxy.newProxyInstance(
                DatabasePool.class.getClassLoader(),
                conn.getClass().getInterfaces(), new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if(method.getName().equals("close")&&isRun) connectionStack.push(myConn);
                        else  return method.invoke(myConn,args);
                        return null;
                    }
                });
    }
    public void close(){
        isRun=false;
        connectionStack.clear();
    }

    public ConnectionStack getConnectionStack() {
        return connectionStack;
    }
}
