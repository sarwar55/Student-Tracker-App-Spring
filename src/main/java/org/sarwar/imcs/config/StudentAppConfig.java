package org.sarwar.imcs.config;

import org.sarwar.imcs.repository.StudentRepository;
import org.sarwar.imcs.repository.StudentRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.sql.DataSource;

/**
 * Created by sarwar on 5/13/17.
 */
@Configuration
@ComponentScan(basePackages = "org.sarwar.imcs")
@EnableWebMvc
public class StudentAppConfig extends WebMvcConfigurerAdapter{

    @Bean
    public ViewResolver getViewResolver(){
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/web_student_tracker?useSSL=false");
        dataSource.setUsername("webstudent");
        dataSource.setPassword("webstudent");
        return dataSource;
    }

    @Bean
    public StudentRepository getStudentRepository() {
        return new StudentRepositoryImpl(getDataSource());
    }
}
