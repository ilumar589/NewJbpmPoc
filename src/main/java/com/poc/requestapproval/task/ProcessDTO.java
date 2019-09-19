package com.poc.requestapproval.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessDTO implements Serializable {
	private int pid;
	private long start;
	private int status;

	public ProcessDTO() {}

	@JsonProperty("process-instance-id")
	public int getPid() {
		return pid;
	}

	@JsonSetter("process-instance-id")
	public void setPid(int pid) {
		this.pid = pid;
	}

	@JsonProperty("start")
	public long getStart() {
		return start;
	}

	@JsonSetter("start")
	public void setStart(long start) {
		this.start = start;
	}

	@JsonProperty("status")
	public int getStatus() {
		return status;
	}

	@JsonSetter("status")
	public void setStatus(int status) {
		this.status = status;
	}
}
