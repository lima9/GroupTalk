package edu.upc.eetac.dsa.grouptalk.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.grouptalk.entity.Theme;
import edu.upc.eetac.dsa.grouptalk.entity.ThemeCollection;

import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ThemeDao
{
    public Theme createTheme(String userid, String groupid, String title, String content) throws SQLException;
    public Theme updateTHeme(String themeid, String content) throws SQLException;
    public Theme getThemeById(String themeid) throws SQLException;
    public ThemeCollection getThemes(long timestamp, boolean before)throws SQLException;
    public boolean deleteTheme(String themeid) throws SQLException;
}
