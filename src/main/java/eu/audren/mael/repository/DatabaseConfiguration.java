package eu.audren.mael.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.io.File;

public class DatabaseConfiguration {

    @Value("${spring.datasource.driverClassName:org.hsqldb.jdbc.JDBCDriver}")
    private String dataSourceDriverClassName;

    @Value("${spring.datasource.url:}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username:root}")
    private String dataSourceUsername;

    @Value("${spring.datasource.password:}")
    private String dataSourcePassword;

    @Bean
    @Profile("default")
    public DataSource defaultDataSource() {
        String jdbcUrl = dataSourceUrl;

        if (jdbcUrl.isEmpty()) {
            jdbcUrl = "jdbc:hsqldb:file:" + getDatabaseDirectory()
                    + ";create=true;hsqldb.tx=mvcc;hsqldb.applog=1;hsqldb.sqllog=0;hsqldb.write_delay=false";
        }

        return DataSourceBuilder
                .create()
                .username(dataSourceUsername)
                .password(dataSourcePassword)
                .url(jdbcUrl)
                .driverClassName(dataSourceDriverClassName)
                .build();
    }

    @Bean
    @Profile("mem")
    public DataSource memDataSource() {
        return createMemDataSource();
    }

    @Bean
    @Profile("test")
    public DataSource testDataSource() {
        return createMemDataSource();
    }

    private DataSource createMemDataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder
                .setType(EmbeddedDatabaseType.HSQL)
                .build();
        return db;
    }

    private String getDatabaseDirectory() {
        return File.separator + "data"
                + File.separator + "db" + File.separator + "home-assignment";
    }

}
