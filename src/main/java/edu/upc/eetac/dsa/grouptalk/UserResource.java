package edu.upc.eetac.dsa.grouptalk;


import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.grouptalk.dao.AuthTokenDaoImpl;
import edu.upc.eetac.dsa.grouptalk.dao.UserAlreadyExistsException;
import edu.upc.eetac.dsa.grouptalk.dao.UserDao;
import edu.upc.eetac.dsa.grouptalk.dao.UserDaoImpl;
import edu.upc.eetac.dsa.grouptalk.entity.AuthToken;
import edu.upc.eetac.dsa.grouptalk.entity.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Path("users")
public class UserResource
{
    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(GroupTalkMediaType.GROUPTALK_AUTH_TOKEN)
    public Response registerUser(@FormParam("loginid") String loginid, @FormParam("password") String password, @FormParam("email") String email, @FormParam("fullname") String fullname, @Context UriInfo uriInfo) throws URISyntaxException {
        if(loginid == null || password == null || email == null || fullname == null)
            throw new BadRequestException("all parameters are mandatory");
        UserDao userDAO = new UserDaoImpl();
        User user = null;
        AuthToken authenticationToken = null;
        try{
            user = userDAO.createUser(loginid, password, email, fullname);
            authenticationToken = (new AuthTokenDaoImpl()).createAuthToken(user.getId());
        }catch (UserAlreadyExistsException e){
            throw new WebApplicationException("loginid already exists", Response.Status.CONFLICT);
        }catch(SQLException e){
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + user.getId());
        return Response.created(uri).type(GroupTalkMediaType.GROUPTALK_AUTH_TOKEN).entity(authenticationToken).build();
    }

    @Path("/{id}")
    @GET
    @Produces(GroupTalkMediaType.GROUPTALK_USER)
    public User getUser(@PathParam("id") String id) {
        User user = null;
        try {
            user = (new UserDaoImpl()).getUserById(id);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(user.getId()))
            throw new ForbiddenException("Solo puedes verte a tí mismo");
        if(user == null)
            throw new NotFoundException("User with id = "+id+" doesn't exist");
        return user;
    }


    @Path("/{id}")
    @PUT
    @Consumes(GroupTalkMediaType.GROUPTALK_USER)
    @Produces(GroupTalkMediaType.GROUPTALK_USER)
    public User updateUser(@PathParam("id") String id, User user)
    {
        if(user == null)
            throw new BadRequestException("entity is null");
        if(!id.equals(user.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        System.out.println(securityContext.getUserPrincipal());
        System.out.println(securityContext.getUserPrincipal().getName());
        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(id))
            throw new ForbiddenException("operation not allowed");

        UserDao userDAO = new UserDaoImpl();
        try {
            user = userDAO.updateProfile(userid, user.getEmail(), user.getFullname());
            if(user == null)
                throw new NotFoundException("User with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return user;
    }


    @Path("/{id}")
    @RolesAllowed("[admin, registered]")
    @DELETE
    public void deleteUser(@PathParam("id") String id){
        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(id))
            throw new ForbiddenException("operation not allowed");
        UserDao userDAO = new UserDaoImpl();
        try {
            if(!userDAO.deleteUser(id))
                throw new NotFoundException("User with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}

