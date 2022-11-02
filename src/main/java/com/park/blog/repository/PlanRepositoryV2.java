package com.park.blog.repository;

import com.park.blog.domain.plan.Plan;
import java.util.List;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Slf4j
public class PlanRepositoryV2 implements PlanRepository{
    private final JdbcTemplate template;

    public PlanRepositoryV2(DataSource dataSource) {
       template = new JdbcTemplate(dataSource);
    }

    public Plan savePlan(Plan plan) {
        String sql = "insert into plan(exercise_name,exercise_set_number,exercise_repetition) values(?,?,?)";
        template.update(sql,plan.getExerciseName(),plan.getExerciseSetNumber(),plan.getExerciseRepetition());
        return plan;
    }

    public List<Plan> findAllPlan() {
        String sql = "select * from plan";
        return template.query(sql,planRowMapper());
    }

    public Plan findPlanByExerciseId(Long exerciseId) {
        String sql = "select * from plan where exercise_id = ?";
        log.info("exerciseId = {}", exerciseId);
        return template.queryForObject(sql,planRowMapper(),exerciseId);
    }

    public void updatePlan(Long exerciseId, Plan plan) {
        String sql = "update plan set exercise_name=? , exercise_set_number=? ," +
            "exercise_repetition=? where exercise_id = ?";
        log.info("V2updatePlan = {}",plan);
        template.update(sql,plan.getExerciseName(),plan.getExerciseSetNumber(),
            plan.getExerciseRepetition(),exerciseId);
    }

    public void deletePlan(Long exerciseId) {
        String sql = "delete plan where exercise_id = ?";

        template.update(sql,exerciseId);
    }

    private RowMapper<Plan> planRowMapper() {
        return (rs,rowNum)-> {

            Plan plan = new Plan();
            plan.setExerciseId(rs.getLong("exercise_id"));
            plan.setExerciseName(rs.getString("exercise_name"));
            plan.setExerciseSetNumber(rs.getInt("exercise_set_number"));
            plan.setExerciseRepetition(rs.getInt("exercise_repetition"));

            return plan;
        };
    }
}
