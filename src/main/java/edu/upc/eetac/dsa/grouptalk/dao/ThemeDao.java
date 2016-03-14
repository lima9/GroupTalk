package edu.upc.eetac.dsa.grouptalk.dao;

import edu.upc.eetac.dsa.grouptalk.entity.Theme;

import java.sql.SQLException;

public interface ThemeDao
{
    public Theme createTheme(String userid, String groupid, String title, String content) throws SQLException;
    public Theme updateTHeme(String themeid, String content) throws SQLException;
    public Theme getThemeById(String themeid) throws SQLException;
    public boolean deleteTheme(String themeid) throws SQLException;
}
