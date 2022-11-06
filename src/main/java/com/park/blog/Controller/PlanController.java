package com.park.blog.Controller;

import com.park.blog.domain.plan.Plan;
import com.park.blog.repository.PlanRepositoryV1;
import com.park.blog.repository.PlanRepositoryV2;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/plans")
public class PlanController {
    private final PlanRepositoryV2 planRepository;

    @GetMapping("/add")
    public String addPlanForm() {
        return "plans/planForm";
    }

    @PostMapping("/add")
    public String addPlan(Plan plan, RedirectAttributes redirectAttributes) throws SQLException {
        Plan savedPlan = planRepository.savePlan(plan);
        //redirect 할때 parameter를 전달받고 싶을 때
        redirectAttributes.addAttribute("planId",savedPlan.getExerciseId());
        redirectAttributes.addAttribute("status",true);
        return "redirect:/";
    }

}
