package edu.upc.eetac.dsa.grouptalk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.org.apache.regexp.internal.RE;
import edu.upc.eetac.dsa.grouptalk.dao.GroupDao;
import edu.upc.eetac.dsa.grouptalk.dao.GroupDaoImpl;
import edu.upc.eetac.dsa.grouptalk.entity.Group;
import edu.upc.eetac.dsa.grouptalk.entity.GroupCollection;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Path("groups")
public class GroupResource
{
    @Context
    private SecurityContext securityContext;

    @POST
    @RolesAllowed("[admin]")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(GroupTalkMediaType.GROUPTALK_GROUP)
    public Response createGroup(@FormParam("name") String name, @Context UriInfo uriInfo) throws URISyntaxException
    {
        if(name==null)
            throw new BadRequestException("all parameters are mandatory");
        GroupDao groupDao = new GroupDaoImpl();
        Group group = null;
        try {
            group = groupDao.createGroup(name);
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + group.getId());
        return Response.created(uri).type(GroupTalkMediaType.GROUPTALK_GROUP).entity(group).build();
    }

    @GET
    @Produces(GroupTalkMediaType.GROUPTALK_GROUP_COLLECTION)
    public GroupCollection getGroups()
    {
        GroupCollection groupCollection;
        GroupDao groupDao = new GroupDaoImpl();
        try
        {
            groupCollection = groupDao.getGroups();
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return groupCollection;
    }

}


/*
public final static String UUID = "select REPLACE(UUID(), '-','')";

    public final static String UPDATE_GROUP = "update groups set name=? where id=unhex(?)";
    public final static String DELETE_GROUP = "delete from groups where id=unhex(?)";
    public final static String GET_GROUP_BY_ID = "select hex(g.id) as id, g.name from groups g where id=unhex(?)";
 */