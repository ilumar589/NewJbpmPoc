package com.poc.requestapproval.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskSummaryWrapper {

	@JsonProperty("taskSummaryList")
	private List<TaskDto> taskSummaries;

	public List<TaskDto> getTaskSummaries() {
		return taskSummaries;
	}

	public void setTaskSummaries(List<TaskDto> taskSummaries) {
		this.taskSummaries = taskSummaries;
	}

	@JsonIgnore
	public Collection<TaskDto> getTasks() {
		return getTaskSummaries();
	}
}

