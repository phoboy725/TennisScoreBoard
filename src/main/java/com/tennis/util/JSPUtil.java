package com.tennis.util;

public class JSPUtil {

    private static final String JSP_PATH_TEMPLATE = "/WEB-INF/views/%s.jsp";

    private JSPUtil(){}

    public static String getJspPath(String pageName){
        return JSP_PATH_TEMPLATE.formatted(pageName);
    }
}
