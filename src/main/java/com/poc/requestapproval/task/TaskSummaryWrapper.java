package com.poc.requestapproval.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskSummaryWrapper {

	@JsonProperty("taskSummaryList")
	private Collection<TaskDto> taskSummaries;

	public Collection<TaskDto> getTaskSummaries() {
		return taskSummaries;
	}

	public void setTaskSummaries(Collection<TaskDto> taskSummaries) {
		this.taskSummaries = taskSummaries;
	}

	@JsonIgnore
	public Collection<TaskDto> getTasks() {
		return getTaskSummaries();
	}
}

