package com.spring.taskAPI.dto;

public class TaskRiskAiDto {
	private long idTask;
	private long daysRemaining;
	private String risk;
	private String title;

	public TaskRiskAiDto(long idTask, long daysRemaining, String risk, String title) {
		super();
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


	public void setDaysRemaining(long daysRemaining) {
		this.daysRemaining = daysRemaining;
	}


	public long getDaysRemaining() {
		return daysRemaining;
	}
	public void setDaysRemaining(int daysRemaining) {
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
