package edu.upc.eetac.dsa.grouptalk;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupTalkMediaType
{
    public final static String GROUPTALK_AUTH_TOKEN = "application/vnd.dsa.grouptalk.auth-token+json";
    public final static String GROUPTALK_USER = "application/vnd.dsa.grouptalk.user+json";
    public final static String GROUPTALK_THEME = "application/vnd.dsa.grouptalk.theme+json";
    public final static String GROUPTALK_THEME_COLLEECTION = "application/vnd.dsa.grouptalk.theme.collection+json";
    public final static String GROUPTALK_GROUP = "application/vnd.dsa.grouptalk.group+json";
    public final static String GROUPTALK_GROUP_COLLECTION = "application/vnd.dsa.grouptalk.group.collection+json";
    public final static String GROUPTALK_ROOT = "application/vnd.dsa.grouptalk.root+json";
}


/*
public class BeeterMediaType
{
    public final static String BEETER_AUTH_TOKEN = "application/vnd.dsa.beeter.auth-token+json";
    public final static String BEETER_USER = "application/vnd.dsa.beeter.user+json";
    public final static String BEETER_STING = "application/vnd.dsa.beeter.sting+json";
    public final static String BEETER_STING_COLLECTION = "application/vnd.dsa.beeter.sting.collection+json";
    public final static String BEETER_ROOT = "application/vnd.dsa.beeter.root+json";
}

 */