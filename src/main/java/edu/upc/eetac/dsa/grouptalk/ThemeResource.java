package edu.upc.eetac.dsa.grouptalk;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.grouptalk.dao.ThemeDao;
import edu.upc.eetac.dsa.grouptalk.dao.ThemeDaoImpl;
import edu.upc.eetac.dsa.grouptalk.entity.Theme;
import edu.upc.eetac.dsa.grouptalk.entity.ThemeCollection;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Path("themes")
public class ThemeResource
{
    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(GroupTalkMediaType.GROUPTALK_THEME)
    public Response createTheme(@FormParam("title") String title, @FormParam("groupid") String groupid, @FormParam("content") String content, @Context UriInfo uriInfo) throws URISyntaxException
    {
        if (title == null || content == null || groupid == null)
            throw new BadRequestException("All parameters are mandatory");
        ThemeDao themeDao = new ThemeDaoImpl();
        Theme theme;
        try
        {
            theme = themeDao.createTheme(securityContext.getUserPrincipal().getName(), groupid, title, content);
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + theme.getId());
        return Response.created(uri).type(GroupTalkMediaType.GROUPTALK_THEME).entity(theme).build();

    }

    @GET
    @Produces(GroupTalkMediaType.GROUPTALK_THEME_COLLEECTION)
    public ThemeCollection getThemes(@QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before)
    {
        ThemeCollection themeCollection = null;
        ThemeDao themeDao = new ThemeDaoImpl();
        try
        {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            themeCollection = themeDao.getThemes(timestamp, before);
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return themeCollection;
    }

    @Path("/{id}")
    @GET
    @Produces(GroupTalkMediaType.GROUPTALK_THEME )
    public Response getTheme(@PathParam("id") String id, @Context Request request)
    {
        // Create cache-control
        CacheControl cacheControl = new CacheControl();
        Theme theme = null;
        ThemeDao themeDAO = new ThemeDaoImpl();
        try
        {
            theme = themeDAO.getThemeById(id);
            if (theme == null)
                throw new NotFoundException("Theme with id = " + id + " doesn't exist");

            // Calculate the ETag on last modified date of user resource
            EntityTag eTag = new EntityTag(Long.toString(theme.getLastModified()));

            // Verify if it matched with etag available in http request
            Response.ResponseBuilder rb = request.evaluatePreconditions(eTag);

            // If ETag matches the rb will be non-null;
            // Use the rb to return the response without any further processing
            if (rb != null)
            {
                return rb.cacheControl(cacheControl).tag(eTag).build();
            }

            // If rb is null then either it is first time request; or resource is
            // modified
            // Get the updated representation and return with Etag attached to it
            rb = Response.ok(theme).cacheControl(cacheControl).tag(eTag);
            return rb.build();
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
    }

    @Path("/{id}")
    @PUT
    @Consumes(GroupTalkMediaType.GROUPTALK_THEME)
    @Produces(GroupTalkMediaType.GROUPTALK_THEME)
    public Theme updateTheme(@PathParam("id") String id, Theme theme)
    {
        if(theme == null)
            throw new BadRequestException("entity is null");
        if(!id.equals(theme.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(theme.getUserid()))
            throw new ForbiddenException("operation not allowed");

        ThemeDao themeDao = new ThemeDaoImpl();
        try
        {
            theme = themeDao.updateTHeme(id, theme.getContent());
            if(theme == null)
                throw new NotFoundException("Theme with id = "+id+" doesn't exist");
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
        return theme;
    }

    @Path("/{id}")
    @RolesAllowed("[admin]")
    @DELETE
    public void deleteTheme(@PathParam("id") String id)
    {
        String userid = securityContext.getUserPrincipal().getName();
        ThemeDao themeDao = new ThemeDaoImpl();
        try
        {
            String ownerid = themeDao.getThemeById(id).getUserid();
            if(!userid.equals(ownerid))
                throw new ForbiddenException("operation not allowed");
            if(!themeDao.deleteTheme(id))
                throw new NotFoundException("Sting with id = "+id+" doesn't exist");
        }
        catch (SQLException e)
        {
            throw new InternalServerErrorException();
        }
    }
}