package com.tennis.controller;

import com.tennis.exception.MissingParameterException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class BaseController extends HttpServlet {
    private static final String PATH_DELIMITER = "/";
    private static final String PARAMETER_PREFIX = "?";
    private static final String PARAMETER_EQUAL = "=";
    private static final String PARAMETER_DELIMITER = "&";
    private static final String JSP_SUB_PATH = "/WEB-INF/views" + PATH_DELIMITER;
    private static final String JSP_EXTENSION = ".jsp";
    private static final String ERROR_ATTRIBUTE = "errorMessage";

    protected void errorRedirect(
            String subPath,
            String message,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        request.getSession().setAttribute(ERROR_ATTRIBUTE, message);
        response.sendRedirect(request.getContextPath() + PATH_DELIMITER + subPath);
    }

    protected void forwardTo(
            String pageName,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.getRequestDispatcher(JSP_SUB_PATH + pageName + JSP_EXTENSION)
                .forward(request, response);
    }

    protected void redirectTo(
            String subPath,
            Map<String, ?> requestParameters,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        String parameters = buildParameters(requestParameters);
        response.sendRedirect(request.getContextPath() + PATH_DELIMITER + subPath + parameters);
    }

    private String buildParameters(Map<String, ?> requestParameters) {
        if (requestParameters.isEmpty()) {
            return "";
        }

        return PARAMETER_PREFIX +
                requestParameters.entrySet().stream()
                        .filter(this::isValidEntry)
                        .map(this::buildParameter)
                        .collect(Collectors.joining(PARAMETER_DELIMITER));
    }

    private boolean isValidEntry(Map.Entry<String, ?> entry) {
        return isNotEmpty(entry.getKey()) && isNotEmpty(entry.getValue());
    }

    private boolean isNotEmpty(Object object) {
        return object != null && !object.toString().isBlank();
    }

    private String buildParameter(Map.Entry<String, ?> parameterEntry) {
        String encodedKey = encodeForUrl(parameterEntry.getKey());
        String encodedValue = encodeForUrl(parameterEntry.getValue().toString());

        return encodedKey + PARAMETER_EQUAL + encodedValue;
    }

    private String encodeForUrl(String raw) {
        return URLEncoder.encode(raw, StandardCharsets.UTF_8);
    }

    String getRequiredParameter(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        if (value == null || value.isBlank()) {
            throw new MissingParameterException("Missing " + paramName);
        }
        return value;
    }

    UUID getUuidParameter(HttpServletRequest request, String paramName) {
        String value = getRequiredParameter(request, paramName);
        if (!value.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$")) {
            throw new MissingParameterException("Missing " + paramName);
        }
        return UUID.fromString(value);
    }
}
