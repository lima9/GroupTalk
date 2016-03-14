package edu.upc.eetac.dsa.grouptalk.dao;

public interface ThemeDAOQuery
{
    public final static String UUID = "select REPLACE(UUID(), '-','')";
    public final static String CREATE_THEME = "insert into themes (id, userid, groupid, title, content) values (UNHEX(?),?,?,?,?);";
    public final static String UPDATE_THEME = "update themes set content=?, where id=unhex(?)";
    public final static String DELETE_THEME = "delete from themes where id=unhex(?)";
    public final static String GET_THEME_BY_ID = "select hex(t.id) as id, t.userid, t.groupid, t.title, t.content from themes t where id=unhex(?)";
}
