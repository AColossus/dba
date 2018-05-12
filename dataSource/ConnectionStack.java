package org.cbb.dba.dataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Colossus on 2018/1/9.
 */
public class ConnectionStack {
    private final int timeout;//单位秒
    private final int capacity;
    private LinkedList<ConnWithTime> list=new LinkedList<ConnWithTime>();

    private class ConnWithTime{
        private final long time=new Date().getTime();
        private Connection conn;
        public ConnWithTime(Connection conn){
            this.conn=conn;
        }
        boolean isTimeout(){
            return new Date().getTime()-time>timeout*1000;
        }
    }

    public ConnectionStack(int capacity,int timeout) {
        this.timeout = timeout;
        this.capacity=capacity;
    }

    private void clearTimeoutConnection(){
        Iterator<ConnWithTime> iterator=list.descendingIterator();
        while(iterator.hasNext()){
            ConnWithTime connWithTime=iterator.next();
            if(connWithTime.isTimeout()){
                try {
                    connWithTime.conn.close();
                    iterator.remove();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else break;
        }
    }

    public synchronized void push(Connection connection) {
        clearTimeoutConnection();
        if(list.size()<capacity)
            list.addFirst(new ConnWithTime(connection));
        else
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    public synchronized Connection pop(){
        if(list.isEmpty())  return null;
        return list.removeFirst().conn;
    }

    public synchronized void clear(){
        Iterator<ConnWithTime> iterator=list.descendingIterator();
        while (iterator.hasNext()){
            ConnWithTime connWithTime=iterator.next();
            try {
                connWithTime.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            iterator.remove();
        }
    }

    public int getTimeout() {
        return timeout;
    }

    public int getCapacity() {
        return capacity;
    }
}
