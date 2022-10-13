package com.park.blog.repository;

import com.park.blog.domain.plan.Plan;
import java.util.List;

public interface PlanRepository {
    Plan savePlan(Plan plan);
    List<Plan> findAllPlan();
    Plan findPlanByExerciseId(Long exerciseId);
    void updatePlan(Long exerciseId, Plan plan);
    void deletePlan(Long exerciseId);
}
