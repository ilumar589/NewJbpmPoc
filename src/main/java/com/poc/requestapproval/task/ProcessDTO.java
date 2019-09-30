package com.poc.requestapproval.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.io.Serializable;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessDTO implements Serializable {

	private static final String PID = "process-instance-id";
	private static final String START_DATE = "start";
	private static final String PROCESS_STATUS = "status";

	private int pid;
	private long start;
	private int status;

	public ProcessDTO() {}

	public ProcessDTO(Map<String, Object> fromJson) {
		pid = (int) fromJson.get(PID);
		start = (long) fromJson.get(START_DATE);
		status = (int) fromJson.get(PROCESS_STATUS);
	}

	@JsonProperty(PID)
	public int getPid() {
		return pid;
	}

	@JsonSetter(PID)
	public void setPid(int pid) {
		this.pid = pid;
	}

	@JsonProperty(START_DATE)
	public long getStart() {
		return start;
	}

	@JsonSetter(START_DATE)
	public void setStart(long start) {
		this.start = start;
	}

	@JsonProperty(PROCESS_STATUS)
	public int getStatus() {
		return status;
	}

	@JsonSetter(PROCESS_STATUS)
	public void setStatus(int status) {
		this.status = status;
	}
}
