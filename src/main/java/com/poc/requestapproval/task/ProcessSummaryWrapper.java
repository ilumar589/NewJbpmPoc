package com.poc.requestapproval.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessSummaryWrapper {

	@JsonProperty("historyLogList")
	private List<ProcessDTO> processSummaries;

	public ProcessSummaryWrapper() {}

	public List<ProcessDTO> getProcessSummaries() {
		return processSummaries;
	}

	public void setProcessSummaries(List<ProcessDTO> processSummaries) {
		this.processSummaries = processSummaries;
	}
}
