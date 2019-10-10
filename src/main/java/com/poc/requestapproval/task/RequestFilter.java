package com.poc.requestapproval.task;

import java.io.Serializable;

public class RequestFilter implements Serializable {

	private String requester;
	private String date;
	private String approver;
	private RequestStatus status;

	public RequestFilter() {

	}

	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}
}
