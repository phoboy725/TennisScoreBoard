package com.tennis.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

public final class RequestParamUtil {

    private RequestParamUtil() {
    }

    public static UUID getRequiredUuid(HttpServletRequest request,
                                       HttpServletResponse response,
                                       String paramName) throws IOException {
        String value = request.getParameter(paramName);
        if (value == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Missing " + paramName);
            return null;
        }

        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid " + paramName);
            return null;
        }
    }

    public static Integer getRequiredInt(HttpServletRequest request,
                                         HttpServletResponse response,
                                         String paramName) throws IOException {
        String value = request.getParameter(paramName);
        if (value == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Missing " + paramName);
            return null;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid " + paramName);
            return null;
        }
    }
}
