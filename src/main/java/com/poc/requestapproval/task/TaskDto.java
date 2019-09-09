package com.poc.requestapproval.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto implements Serializable {

	private long id;
	private String name;
	private String description;
	private String status;
	private String owner;
	private Long createdOn;
	private String deploymentId;
	private String processId;

	protected TaskDto() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {

		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("taskOwner")
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * Alias for owner used by jBPM
	 *
	 * @param actualOwner
	 */
	@JsonSetter("actual-owner")
	public void setActualOwner(String actualOwner) {
		if (this.owner == null) {
			this.owner = actualOwner;
		}
	}

	@JsonProperty("created-on")
	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	@JsonProperty("deployment-id")
	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	@JsonProperty("process-id")
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public static class Builder {

		private Long id;
		private String name;
		private String description;
		private String status;
		private Long createdOn;
		private String deploymentId;
		private String processId;

		private Builder() {
		}

		public static Builder taskDto() {
			return new Builder();
		}

		public Builder withId(long id) {
			this.id = id;
			return this;
		}

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder withStatus(String status) {
			this.status = status;
			return this;
		}

		public Builder withCreatedOn(Long createdOn) {
			this.createdOn = createdOn;
			return this;
		}

		public Builder withDeploymentId(String deploymentId) {
			this.deploymentId = deploymentId;
			return this;
		}

		public Builder withProcessId(String processId) {
			this.processId = processId;
			return this;
		}

		public TaskDto build() {
			TaskDto task = new TaskDto();
			task.id = id;
			task.name = name;
			task.description = description;
			task.status = status;
			task.createdOn = createdOn;
			task.deploymentId = deploymentId;
			task.processId = processId;
			return task;
		}
	}
}
