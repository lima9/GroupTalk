package edu.upc.eetac.dsa.grouptalk;

import edu.upc.eetac.dsa.grouptalk.entity.GroupTalkRootAPI;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class GroupTalkRootAPIResource
{
    @Context
    private SecurityContext securityContext;

    private String userid;


    @GET
    @Produces(GroupTalkMediaType.GROUPTALK_ROOT)
    public GroupTalkRootAPI getRootAPI()
    {
        if (securityContext.getUserPrincipal()!=null)
            userid = securityContext.getUserPrincipal().getName();
        GroupTalkRootAPI groupTalkRootAPI = new GroupTalkRootAPI();

        return groupTalkRootAPI;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}