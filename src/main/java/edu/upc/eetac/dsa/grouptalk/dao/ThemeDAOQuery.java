package edu.upc.eetac.dsa.grouptalk.dao;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ThemeDAOQuery
{
    public final static String UUID = "select REPLACE(UUID(), '-','')";
    public final static String CREATE_THEME = "insert into themes (id, userid, groupid, title, content) values (UNHEX(?),?,?,?,?);";
    public final static String UPDATE_THEME = "update themes set content=?, where id=unhex(?)";
    public final static String GET_THEMES = "select hex(id) as id, hex(userid) as userid, subject, creation_timestamp, last_modified from themes where creation_timestamp < ? order by creation_timestamp desc limit 5";
    public final static String DELETE_THEME = "delete from themes where id=unhex(?)";
    public final static String GET_THEME_BY_ID = "select hex(t.id) as id, t.userid, t.groupid, t.title, t.content from themes t where id=unhex(?)";
    public final static String GET_THEMES_AFTER = "select hex(id) as id, hex(userid) as userid, subject, creation_timestamp, last_modified from themes  where creation_timestamp > ? order by creation_timestamp desc limit 5";

}
