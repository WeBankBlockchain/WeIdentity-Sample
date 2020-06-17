package com.webank.weid.demo.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.webank.weid.demo.filter.XssFilter;

/**
 * web mvc support.
 * @author darwindu
 * @date 2020/1/16
 **/
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     * 跨域过滤器.
     * @return
     */
    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        config.setAllowCredentials(false);
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);

        return bean;
    }
    
    /**
     * XSS过滤器.
     * @return
     */
    @Bean
    public FilterRegistrationBean xssFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        config.setAllowCredentials(false);
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean bean = new FilterRegistrationBean(new XssFilter());
        bean.setOrder(1);

        return bean;
    }
}
