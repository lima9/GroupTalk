package edu.upc.eetac.dsa.grouptalk.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.grouptalk.auth.UserInfo;
import edu.upc.eetac.dsa.grouptalk.entity.AuthToken;

import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface AuthTokenDao
{
    public UserInfo getUserByAuthToken(String token) throws SQLException;
    public AuthToken createAuthToken(String userid) throws SQLException;
    public void deleteToken(String userid) throws SQLException;
}
