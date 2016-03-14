package edu.upc.eetac.dsa.grouptalk.dao;

import edu.upc.eetac.dsa.grouptalk.entity.Group;

import java.sql.SQLException;

public interface GroupDao
{
    public Group createGroup(String name) throws SQLException;
    public Group updateGroup(String groupid, String name) throws SQLException;
    public Group getGroupById(String groupid) throws SQLException;
    public boolean deleteGroup(String groupid) throws SQLException;
}
