package com.spring.taskAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class TaskRiskAiDto {

    @Schema(description = "task id", example = "5")
    private long idTask;

    @Schema(description = "days remaining until the deadline", example = "2")
    private long daysRemaining;

    @Schema(description = "risk of the task expiring", example = "HIGH")
    private String risk;

    @Schema(description = "task title", example = "Wash the dishes")
    private String title;

    public TaskRiskAiDto(long idTask, long daysRemaining, String risk, String title) {
        this.idTask = idTask;
        this.daysRemaining = daysRemaining;
        this.risk = risk;
        this.title = title;
    }

    public long getIdTask() {
        return idTask;
    }

    public void setIdTask(long idTask) {
        this.idTask = idTask;
    }

    public long getDaysRemaining() {
        return daysRemaining;
    }

    public void setDaysRemaining(long daysRemaining) {
        this.daysRemaining = daysRemaining;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}