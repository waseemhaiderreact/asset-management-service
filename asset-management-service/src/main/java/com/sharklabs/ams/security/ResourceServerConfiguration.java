//package com.sharklabs.ams.security;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpMethod;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import javax.sql.DataSource;
//
//
//@Configuration
//@EnableGlobalMethodSecurity(
//        prePostEnabled = true
//)
//public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
//
//    @Value("${spring.datasource.driver-class-name}")
//    private String dataSourceDriver="";
//
//    @Value("${spring.datasource.url}")
//    private String dataSourceUrl="";
//
//    @Value("${spring.datasource.username}")
//    private String dataSourceUserName="";
//
//    @Value("${spring.datasource.password}")
//    private String dataSourcePassword="";
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception{
////        http
////                .authorizeRequests()
////                .antMatchers(HttpMethod.DELETE, "/quotes/**","/assets/*")
////                .hasRole("ADMIN")
////                .anyRequest()
////                .authenticated();
//
////        http.authorizeRequests().anyRequest().authenticated();
//
//        http.authorizeRequests()
//                .antMatchers("/assets/inventory/asset/assign","/assets/inventory/asset/count","/inventory/asset/location","/assets/inventory/marked","/assets/inspections/assetName","/assets/inspections/assetCategory","/assets/inventory/detail","/assets/inventory/quantity").permitAll()
//                .anyRequest().fullyAuthenticated();
//        http
//                .csrf().disable();
//    }
//
//    @Bean
//    public FilterRegistrationBean corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("*");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        source.registerCorsConfiguration("/**", config);
//        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//        // bean.setOrder(0);
//        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        return bean;
//    }
//
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//
//        dataSource.setDriverClassName(dataSourceDriver);
//        dataSource.setUrl(dataSourceUrl);
//        dataSource.setUsername(dataSourceUserName);
//        dataSource.setPassword(dataSourcePassword);
//        return dataSource;
//    }
//}