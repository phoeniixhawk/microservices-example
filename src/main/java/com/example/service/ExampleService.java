package com.example.service;

import com.example.ExampleMessageResource;
import com.example.domain.ExampleRequest;
import com.example.domain.ExampleResponse;
import com.example.mappers.ExampleMapperClass;
import com.example.mybatis.entity.ExamplePojo;
import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.Reader;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ExampleService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private static ExampleMessageResource exampleMessageResource;

    @Value("${db.driverClass}")
    private String driver;
    @Value("${db.connectionUrl}")
    private String url;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;

//    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    public ExampleResponse index(String name) throws Exception {
        ExampleResponse response = null;
        final String template = exampleMessageResource.getMessage("application.welcome") + ", %s!";
        try {
            logger.debug("Attempting to process param endpoint.");
            return new ExampleResponse(counter.incrementAndGet(),
                    String.format(template, name));
        } catch (Exception exception) {
            logger.debug("Processor failed.", exception);
        }
        return response;
    }

    public ExampleResponse getRecord(ExampleRequest request) throws Exception {
        ExampleResponse response = null;
        try {
            logger.debug("Attempting to process body endpoint");

            Properties properties = new Properties();
            properties.setProperty("driver", driver);
            properties.setProperty("url", url);
            properties.setProperty("username", username);
            properties.setProperty("password", password);

            ExamplePojo examplePojo;

//            String resource = "com/example/config/mybatis-config.xml";
////            InputStream inputStream = Resources.getResourceAsStream(resource);
////            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//            Reader reader = Resources.getResourceAsReader(resource);
//            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, properties);
//            SqlSession session = sqlSessionFactory.openSession();
//
//            try {
//                examplePojo = session.selectOne("com.example.mappers.ExampleMapperClass.selectExample", request.getId());
//            } finally {
//                session.close();
//            }

            PooledDataSourceFactory pooledDataSourceFactory = new PooledDataSourceFactory();
            pooledDataSourceFactory.setProperties(properties);
            DataSource dataSource = pooledDataSourceFactory.getDataSource();

            TransactionFactory transactionFactory = new JdbcTransactionFactory();
            Environment environment = new Environment("example", transactionFactory, dataSource);
            Configuration configuration = new Configuration(environment);
            configuration.addMapper(ExampleMapperClass.class);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
            SqlSession session = sqlSessionFactory.openSession();

            try {
                ExampleMapperClass exampleMapperClass = session.getMapper(ExampleMapperClass.class);
                examplePojo = exampleMapperClass.getExamplePojo(request.getId());
            } finally {
                session.close();
            }

            response = new ExampleResponse(examplePojo.getId(), examplePojo.getContent());
        } catch (Exception exception) {
            logger.debug("Processor failed.", exception);
        }
        return response;
    }

//    private Properties getDbProperties(String driverClass, String connectionUrl, String username, String password){
//        return properties;
//    }
}