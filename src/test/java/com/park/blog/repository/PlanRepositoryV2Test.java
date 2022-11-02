package com.park.blog.repository;

import static com.park.blog.connection.ConnectionConst.PASSWORD;
import static com.park.blog.connection.ConnectionConst.URL;
import static com.park.blog.connection.ConnectionConst.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.park.blog.domain.plan.Plan;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

@Slf4j
class PlanRepositoryV2Test {

    PlanRepositoryV2 repository;

    @BeforeEach
    void beforeEach() {
        //기본 DriverManager
        //DriverManagerDataSource dataSource = new DriverManagerDataSource(URL,USERNAME,PASSWORD);

        //커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        repository = new PlanRepositoryV2(dataSource);
    }

    @Test
    @DisplayName("운동 입력")
    void add() throws SQLException {
        Plan plan = new Plan("push-up",3,10);
        repository.savePlan(plan);
    }

    @Test
    @DisplayName("아이디로 운동찾기")
    void findById() {
        Plan p = repository.findPlanByExerciseId(80L);
        log.info("findById = {}",p);
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
        Plan savePlan= repository.savePlan(plan);
        savePlan.setExerciseId(100L);
        log.info("savePlan = {}",savePlan.toString());
        //when
        Plan updatePlan = new Plan("윗몸 일으키기",1,5);
        repository.updatePlan(savePlan.getExerciseId(),updatePlan);
        log.info("updatePlan = {}",updatePlan.toString());

        //then
        Plan findPlan = repository.findPlanByExerciseId(savePlan.getExerciseId());
        log.info("findPlan = {}",findPlan.toString());
        assertThat(findPlan.getExerciseName()).isEqualTo(updatePlan.getExerciseName());
        assertThat(findPlan.getExerciseSetNumber()).isEqualTo(updatePlan.getExerciseSetNumber());
        assertThat(findPlan.getExerciseRepetition()).isEqualTo(updatePlan.getExerciseRepetition());

    }

    @Test
    @DisplayName("운동계획 지우기")
    void delete() throws SQLException {
        Plan findPlan = repository.findPlanByExerciseId(69L);
        log.info("{}",findPlan.toString());
        repository.deletePlan(findPlan.getExerciseId());

        assertThatThrownBy(()->repository.findPlanByExerciseId(findPlan.getExerciseId()))
            .isInstanceOf(EmptyResultDataAccessException.class);
    }
}