package com.poc.requestapproval.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessDTO {
	private long pid;
	//TODO date

	public ProcessDTO() {}

	@JsonProperty("process-instance-id")
	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}
}
