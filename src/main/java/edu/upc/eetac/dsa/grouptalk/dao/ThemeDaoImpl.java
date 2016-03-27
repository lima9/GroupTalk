package edu.upc.eetac.dsa.grouptalk.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.grouptalk.entity.Theme;
import edu.upc.eetac.dsa.grouptalk.entity.ThemeCollection;

import java.sql.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ThemeDaoImpl implements ThemeDao
{

    @Override
    public Theme createTheme(String userid, String groupid, String title, String content) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try {
            Theme theme = getThemeById(userid);

            connection = Database.getConnection();

            stmt = connection.prepareStatement(ThemeDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                id = rs.getString(1);
            else
                throw new SQLException();

            connection.setAutoCommit(false);

            stmt.close();
            stmt = connection.prepareStatement(ThemeDAOQuery.CREATE_THEME);
            stmt.setString(1, id);
            stmt.setString(2, userid);
            stmt.setString(3, groupid);
            stmt.setString(4, title);
            stmt.setString(5, content);
            stmt.executeUpdate();

            connection.commit();
        }
        catch (SQLException e)
        {
            throw e;
        }
        finally {
            if (stmt !=null) stmt.close();
            if(connection != null)
            {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return getThemeById(id);
    }

    @Override
    public Theme updateTHeme(String themeid, String content) throws SQLException {
        Theme theme = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(ThemeDAOQuery.UPDATE_THEME);
            stmt.setString(1, content);
            stmt.setString(2, themeid);

            int rows = stmt.executeUpdate();
            if (rows == 1)
                theme = getThemeById(themeid);
        }
        catch (SQLException e)
        {
            throw e;
        }finally {
            if (stmt != null) stmt.close();
            if (connection !=null) connection.close();
        }
        return theme;
    }

    @Override
    public Theme getThemeById(String themeid) throws SQLException {
        Theme theme = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            // Obtiene la conexión del DataSource
            connection = Database.getConnection();

            // Prepara la consulta
            stmt = connection.prepareStatement(ThemeDAOQuery.GET_THEME_BY_ID);
            // Da valor a los parámetros de la consulta
            stmt.setString(1, themeid);

            // Ejecuta la consulta
            ResultSet rs = stmt.executeQuery();
            // Procesa los resultados
            if (rs.next()) {
                theme = new Theme();
                theme.setId(rs.getString("id"));
                theme.setUserid(rs.getString("userid"));
                theme.setGroupid(rs.getString("groupid"));
                theme.setTitle(rs.getString("title"));
                theme.setContent(rs.getString("content"));
            }
        } catch (SQLException e) {
            // Relanza la excepción
            throw e;
        } finally {
            // Libera la conexión
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        // Devuelve el modelo
        return theme;
    }

    @Override
    public boolean deleteTheme(String themeid) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(ThemeDAOQuery.DELETE_THEME);
            stmt.setString(1, themeid);

            int rows = stmt.executeUpdate();
            return (rows == 1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public ThemeCollection getThemes(long timestamp, boolean before) throws SQLException {
        ThemeCollection themeCollection = new ThemeCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            if(before)
                stmt = connection.prepareStatement(ThemeDAOQuery.GET_THEMES);
            else
                stmt = connection.prepareStatement(ThemeDAOQuery.GET_THEMES_AFTER);
            stmt.setTimestamp(1, new Timestamp(timestamp));

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                Theme theme = new Theme();
                theme.setId(rs.getString("id"));
                theme.setUserid(rs.getString("userid"));
                theme.setContent(rs.getString("content"));
                theme.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                theme.setLastModified(rs.getTimestamp("last_modified").getTime());
                if (first) {
                    themeCollection.setNewestTimestamp(theme.getLastModified());
                    first = false;
                }
                themeCollection.setOldestTimestamp(theme.getLastModified());
                themeCollection.getThemes().add(theme);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return themeCollection;
    }
}