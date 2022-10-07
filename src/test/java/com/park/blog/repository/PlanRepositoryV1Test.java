package com.park.blog.repository;

import static com.park.blog.connection.ConnectionConst.PASSWORD;
import static com.park.blog.connection.ConnectionConst.URL;
import static com.park.blog.connection.ConnectionConst.USERNAME;
import static org.assertj.core.api.Assertions.*;

import com.park.blog.domain.plan.Plan;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
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
    @DisplayName("운동 입력")
    void CRUD() throws SQLException {
        Plan plan = new Plan("push-up",3,10);
        repository.savePlan(plan);
    }

    @Test
    @DisplayName("전체 운동목록 가져오기")
    void findAll() {
        List<Plan> plans = repository.findAllPlan();
        log.info("plan size = {}",plans.size());
        assertThat(plans).hasSize(1);
    }

    @Test
    @DisplayName("운동 업데이트")
    void update() throws SQLException {
        //given
        Plan plan = new Plan("런지",10,10);
        plan.setExerciseId(100L);
        Plan savePlan= repository.savePlan(plan);
        log.info("{}",plan.toString());
        //when
        Plan updatePlan = new Plan("윗몸 일으키기",5,5);
        repository.updatePlan(savePlan.getExerciseId(),updatePlan);

        //then
        Plan findPlan = repository.findPlanByExerciseId(savePlan.getExerciseId());
        assertThat(findPlan.getExerciseName()).isEqualTo(updatePlan.getExerciseName());
        assertThat(findPlan.getExerciseSetNumber()).isEqualTo(updatePlan.getExerciseSetNumber());
        assertThat(findPlan.getExerciseRepetition()).isEqualTo(updatePlan.getExerciseRepetition());
    }

    @Test
    @DisplayName("운동계획 지우기")
    void delete() throws SQLException {
        Plan findPlan = repository.findPlanByExerciseId(37L);
        log.info("{}",findPlan.toString());
        repository.deletePlan(findPlan.getExerciseId());

        assertThat(repository.findAllPlan().size()).isEqualTo(0);
    }
}