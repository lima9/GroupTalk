package edu.upc.eetac.dsa.grouptalk;


import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.grouptalk.dao.AuthTokenDao;
import edu.upc.eetac.dsa.grouptalk.dao.AuthTokenDaoImpl;
import edu.upc.eetac.dsa.grouptalk.dao.UserDao;
import edu.upc.eetac.dsa.grouptalk.dao.UserDaoImpl;
import edu.upc.eetac.dsa.grouptalk.entity.AuthToken;
import edu.upc.eetac.dsa.grouptalk.entity.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Path("login")
public class LoginResource
{
    @Context
    SecurityContext securityContext;
    @POST
    @RolesAllowed("[admin.registered]")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(GroupTalkMediaType.GROUPTALK_AUTH_TOKEN)
    public AuthToken login(@FormParam("login") String loginid, @FormParam("password") String password) {
        if(loginid == null || password == null)
            throw new BadRequestException("all parameters are mandatory");

        User user = null;
        AuthToken authToken = null;
        try{
            UserDao userDAO = new UserDaoImpl();
            user = userDAO.getUserByLoginid(loginid);
            if(user == null)
                throw new BadRequestException("loginid " + loginid + " not found.");
            if(!userDAO.checkPassword(user.getId(), password))
                throw new BadRequestException("incorrect password");

            AuthTokenDao authTokenDAO = new AuthTokenDaoImpl();
            authTokenDAO.deleteToken(user.getId());
            authToken = authTokenDAO.createAuthToken(user.getId());
        }catch(SQLException e){
            throw new InternalServerErrorException();
        }
        return authToken;
    }

    @DELETE
    @RolesAllowed("[admin.registered]")
    public void logout(){
        String userid = securityContext.getUserPrincipal().getName();
        AuthTokenDao authTokenDAO = new AuthTokenDaoImpl();
        try {
            authTokenDAO.deleteToken(userid);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}
