package com.webank.weid.demo.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringEscapeUtils;


public class XssHttpServletRequestWraper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWraper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String str = StringEscapeUtils.escapeHtml(super.getHeader(name));
        return str;
    }

    @Override
    public String getQueryString() {
        String str = StringEscapeUtils.escapeHtml(super.getQueryString());
        return  str;
    }

    @Override
    public String getParameter(String name) {
        String str = StringEscapeUtils.escapeHtml(super.getParameter(name));
        return str;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if(values != null) {
            int length = values.length;
            String[] escapseValues = new String[length];
            for(int i = 0; i < length; i++){
                escapseValues[i] = StringEscapeUtils.escapeHtml(values[i]);
            }
            return escapseValues;
        }
        return super.getParameterValues(name);
    }
}