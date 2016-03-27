package edu.upc.eetac.dsa.grouptalk.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface GroupDAOQuery
{
    public final static String UUID = "select REPLACE(UUID(), '-','')";
    public final static String CREATE_GROUP = "insert into groups (id, name) values (UNHEX(?),?);";
    public final static String UPDATE_GROUP = "update groups set name=? where id=unhex(?)";
    public final static String DELETE_GROUP = "delete from groups where id=unhex(?)";
    public final static String GET_GROUPS = "select hex(id) as id from groups where id=unhex(?)";
    public final static String GET_GROUP_BY_ID = "select hex(g.id) as id, g.name from groups g where id=unhex(?)";
}