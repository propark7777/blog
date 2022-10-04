package com.park.blog.repository;

import static com.park.blog.connection.ConnectionConst.PASSWORD;
import static com.park.blog.connection.ConnectionConst.URL;
import static com.park.blog.connection.ConnectionConst.USERNAME;

import com.park.blog.domain.plan.Plan;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlanRepositoryV1Test {

    PlanRepositoryV1 repository;

    @BeforeEach
    void beforeEach() {
        //기본 DriverManager
        //DriverManagerDataSource dataSource = new DriverManagerDataSource(URL,USERNAME,PASSWORD);

        //커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        repository = new PlanRepositoryV1(dataSource);
    }

    @Test
    void CRUD() throws SQLException {
        Plan plan = new Plan("push-up",3,10);
        repository.savePlan(plan);
    }
}