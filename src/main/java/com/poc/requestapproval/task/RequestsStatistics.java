package com.poc.requestapproval.task;

import java.io.Serializable;

public class RequestsStatistics implements Serializable {

	private Long approvedRequests;
	private Long pendingRequests;
	private Long declinedRequests;

	public RequestsStatistics() {
	}

	public RequestsStatistics(Long approvedRequests, Long pendingRequests, Long declinedRequests) {
		this.approvedRequests = approvedRequests;
		this.pendingRequests = pendingRequests;
		this.declinedRequests = declinedRequests;
	}

	public Long getApprovedRequests() {
		return approvedRequests;
	}

	public void setApprovedRequests(Long approvedRequests) {
		this.approvedRequests = approvedRequests;
	}

	public Long getPendingRequests() {
		return pendingRequests;
	}

	public void setPendingRequests(Long pendingRequests) {
		this.pendingRequests = pendingRequests;
	}

	public Long getDeclinedRequests() {
		return declinedRequests;
	}

	public void setDeclinedRequests(Long declinedRequests) {
		this.declinedRequests = declinedRequests;
	}
}
