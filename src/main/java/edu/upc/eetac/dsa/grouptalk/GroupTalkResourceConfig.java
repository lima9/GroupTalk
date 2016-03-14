package edu.upc.eetac.dsa.grouptalk;

import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

public class GroupTalkResourceConfig extends ResourceConfig
{
    public GroupTalkResourceConfig()
    {

        packages("edu.upc.eetac.dsa.grouptalk");
        packages("edu.upc.eetac.dsa.grouptalk.auth");
        packages("edu.upc.eetac.dsa.grouptalk.cors");
        register(RolesAllowedDynamicFeature.class);
    }
}
