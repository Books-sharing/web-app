package com.ninjabooks.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "com.ninjabooks")
public class AppConfig extends WebMvcConfigurerAdapter
{
    private final ApplicationContext applicationContext;

    @Autowired
    public AppConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**/*", "/**")
            .addResourceLocations("/WEB-INF/static/assets/", "/WEB-INF/static/");
    }

    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/");
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(deviceConfigBean().deviceHandlerMethodArgumentResolver());
        argumentResolvers.add(deviceConfigBean().sitePreferenceHandlerMethodArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(deviceConfigBean().deviceResolverHandlerInterceptor());
        registry.addInterceptor(deviceConfigBean().sitePreferenceHandlerInterceptor());
    }

    @Override
    public Validator getValidator() {
        ValidatorConfig bean = applicationContext.getBean(ValidatorConfig.class);
        return (Validator) bean.validator();
    }

    private DeviceConfig deviceConfigBean() {
        return applicationContext.getBean(DeviceConfig.class);
    }
}
