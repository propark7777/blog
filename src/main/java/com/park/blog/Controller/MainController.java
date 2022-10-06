package com.park.blog.Controller;

import com.park.blog.domain.plan.Plan;
import com.park.blog.repository.PlanRepositoryV1;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final PlanRepositoryV1 planRepository;

    @GetMapping("/main")
    public String main(){
        return "main";
    }

    @GetMapping("/add")
    public String addPlanForm() {
        return "planForm";
    }

    @PostMapping("/add")
    public String addPlan(Plan plan, RedirectAttributes redirectAttributes) throws SQLException {
        Plan savedPlan = planRepository.savePlan(plan);
        redirectAttributes.addAttribute("planId",savedPlan.getExerciseId());
        redirectAttributes.addAttribute("status",true);
        return "main";
    }

}
