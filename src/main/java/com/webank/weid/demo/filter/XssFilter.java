package com.webank.weid.demo.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class XssFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(
        ServletRequest servletRequest, 
        ServletResponse servletResponse, 
        FilterChain filterChain
    ) throws IOException, ServletException {
        filterChain.doFilter(
            new XssHttpServletRequestWraper((HttpServletRequest) servletRequest),
            servletResponse
        );
    }

    @Override
    public void destroy() {

    }
}