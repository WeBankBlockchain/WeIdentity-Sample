package com.webank.weid.demo.config;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.webank.weid.demo.filter.XssFilter;

/**
 * web mvc support.
 * @author darwindu
 * @date 2020/1/16
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 跨域过滤器.
     * @return
     */
    @Bean
    public FilterRegistrationBean<Filter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        config.setAllowCredentials(false);
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<Filter>(
            new CorsFilter(source));
        bean.setOrder(0);

        return bean;
    }
    
    /**
     * XSS过滤器.
     * @return
     */
    @Bean
    public FilterRegistrationBean<Filter> xssFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        config.setAllowCredentials(false);
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<Filter>(new XssFilter());
        bean.setOrder(1);

        return bean;
    }
}
