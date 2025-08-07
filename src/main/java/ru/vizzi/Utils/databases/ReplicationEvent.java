package ru.vizzi.Utils.databases;

import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;

public interface ReplicationEvent {

    long getServerID();

    void writeEvent(String databases, String table, WriteRowsEventData rowsEventData);
    void updateEvent(String databases, String table, UpdateRowsEventData rowsEventData);
    void deleteEvent(String databases, String table, DeleteRowsEventData rowsEventData);


}
