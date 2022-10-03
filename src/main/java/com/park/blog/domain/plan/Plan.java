package com.park.blog.domain.plan;

import lombok.Data;

@Data
public class Plan {
    private Long exerciseId;
    private String exerciseName;
    private int exerciseSetNumber;
    private int exerciseRepetition;

    public Plan() {
    }

    public Plan(String exerciseName, int exerciseSetNumber, int exerciseRepetition) {
        this.exerciseName = exerciseName;
        this.exerciseSetNumber = exerciseSetNumber;
        this.exerciseRepetition = exerciseRepetition;
    }
}
