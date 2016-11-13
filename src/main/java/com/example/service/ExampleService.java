package com.example.service;

import com.example.ExampleMessageResource;
import com.example.domain.ExampleRequest;
import com.example.domain.ExampleResponse;
import com.example.mappers.ExampleMapperClass;
import com.example.mybatis.entity.ExamplePojo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ExampleService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private static ExampleMessageResource exampleMessageResource;

//    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    public ExampleResponse process(String name) throws Exception {
        ExampleResponse response = null;
        final String template = exampleMessageResource.getMessage("application.welcome") + ", %s!";
        try {
            logger.debug("Attempting to process param endpoint.");
            return new ExampleResponse(counter.incrementAndGet(),
                    String.format(template, name));
        } catch (Exception poiException) {
            logger.debug("POI Processor failed.", poiException);
        }
        return response;
    }

    public ExampleResponse process(ExampleRequest request) throws Exception {
        ExampleResponse response = null;
        try {
            logger.debug("Attempting to process body endpoint");

            String resource = "org/mybatis/example/mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession session = sqlSessionFactory.openSession();
            ExamplePojo blog;
            try {
                ExampleMapperClass mapper = session.getMapper(ExampleMapperClass.class);
                blog = mapper.getExamplePojo(101, "");
            } finally {
                session.close();
            }

            response = new ExampleResponse(blog.getId(), blog.getContent());
        } catch (Exception poiException) {
            logger.debug("POI Processor failed.", poiException);
        }
        return response;
    }
}