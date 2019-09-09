package com.poc.requestapproval.task;

import java.io.Serializable;
import java.util.Date;

public class TaskRequest implements Serializable {

	private Long requesterId;
	private Long firstApprover;
	private Date date;

	public Long getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(Long requesterId) {
		this.requesterId = requesterId;
	}

	public Long getFirstApprover() {
		return firstApprover;
	}

	public void setFirstApprover(Long firstApprover) {
		this.firstApprover = firstApprover;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
