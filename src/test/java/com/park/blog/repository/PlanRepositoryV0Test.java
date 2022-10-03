package com.park.blog.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.park.blog.domain.plan.Plan;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

class PlanRepositoryV0Test {

    PlanRepositoryV0 repository = new PlanRepositoryV0();

    @Test
    void CRUD() throws SQLException {
        Plan plan = new Plan("squat",5,20);
        repository.savePlan(plan);
    }
}