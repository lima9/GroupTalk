package edu.upc.eetac.dsa.grouptalk;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
