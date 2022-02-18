package com.pet.common.filter;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CookieAttributeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        chain.doFilter(request, response);
        log.info("CookieAttributeFilter");

        Collection<String> headers = httpServletResponse.getHeaders(HttpHeaders.SET_COOKIE);
        boolean firstHeader = true;

        for (String header : headers) {
            if (firstHeader) {
                httpServletResponse
                    .setHeader(HttpHeaders.SET_COOKIE, String.format("%s;Secure;%s", header, "SameSite=" + "None"));
                firstHeader = false;
                continue;
            }
            httpServletResponse
                .addHeader(HttpHeaders.SET_COOKIE, String.format("%s;Secure;%s", header, "SameSite=" + "None"));
        }
    }
}
