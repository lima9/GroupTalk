package edu.upc.eetac.dsa.grouptalk.entity;

import javax.ws.rs.core.Link;
import java.util.List;

public class Group
{
    private List<Link> links;
    private String id;
    private String name;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
