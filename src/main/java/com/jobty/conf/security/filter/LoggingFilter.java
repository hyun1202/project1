package com.jobty.conf.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class LoggingFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        boolean isServletReqOrRes = servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse;
        // swagger 로깅 제외
        if (!isServletReqOrRes) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }
        String url =  ((HttpServletRequest) servletRequest).getRequestURI();
        if (Pattern.matches("^\\/(swagger-ui|v3\\/api-docs)+\\/[\\w\\d-._\\/]*]*$", url)){
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (Pattern.matches("^[-._=~!@#/$\\w\\d]+\\.(png|jpg|jpeg|svg|gif)$", url)){
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpServletRequest requestToCache = new ContentCachingRequestWrapper(request);
        HttpServletResponse responseToCache = new ContentCachingResponseWrapper(response);

        chain.doFilter(requestToCache, responseToCache);

        logger.info("request url: {}", request.getRequestURI());
        logger.info("request header: {}", getHeaders(requestToCache));
        logger.info("request body: {}", getRequestBody((ContentCachingRequestWrapper) requestToCache));
        logger.info("response body: {}", getResponseBody(responseToCache));
    }

    private Map<String, Object> getHeaders(HttpServletRequest request) {
        Map<String, Object> headerMap = new HashMap<>();

        Enumeration<String> headerArray = request.getHeaderNames();
        while (headerArray.hasMoreElements()) {
            String headerName = headerArray.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    return " - ";
                }
            }
        }
        return " - ";
    }

    private String getResponseBody(final HttpServletResponse response) throws IOException {
        String payload = null;
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            wrapper.setCharacterEncoding("UTF-8");
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                wrapper.copyBodyToResponse();
            }
        }
        return null == payload ? " - " : payload;
    }
}