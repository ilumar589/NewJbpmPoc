package com.poc.requestapproval.task;

import java.util.List;


public class ProcessSummaryWrapper {
	private List<ProcessDTO> processSummaries;

	public ProcessSummaryWrapper() {}

	public List<ProcessDTO> getProcessSummaries() {
		return processSummaries;
	}

	public void setProcessSummaries(List<ProcessDTO> processSummaries) {
		this.processSummaries = processSummaries;
	}
}
