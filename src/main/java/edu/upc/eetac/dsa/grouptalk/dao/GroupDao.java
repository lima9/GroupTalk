package edu.upc.eetac.dsa.grouptalk.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.grouptalk.entity.Group;
import edu.upc.eetac.dsa.grouptalk.entity.GroupCollection;

import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface GroupDao
{
    Group createGroup(String name) throws SQLException;
    Group updateGroup(String groupid, String name) throws SQLException;
    Group getGroupById(String groupid) throws SQLException;
    GroupCollection getGroups() throws SQLException;
    boolean deleteGroup(String groupid) throws SQLException;
}
