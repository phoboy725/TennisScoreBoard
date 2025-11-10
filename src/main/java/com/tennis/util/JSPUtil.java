package com.tennis.util;

public class JSPUtil {

    private static final String JSP_PATCH = "/WEB-INF/views/%s.jsp";

    private JSPUtil(){}

    public static String getJspPatch(String pageName){
        return JSP_PATCH.formatted(pageName);
    }
}
