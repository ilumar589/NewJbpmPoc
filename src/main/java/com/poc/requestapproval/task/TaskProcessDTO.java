package com.poc.requestapproval.task;

import java.io.Serializable;
import java.util.Date;

public class TaskProcessDTO implements Serializable {

	private Long processInstanceId;
	private Long taskId;
	private Long requesterId;
	private String requesterName;
	private Date date;
	private Long firstApproverId;
	private String firstApproverName;
	private String firstApproverStatus;
	private Long secondApproverId;
	private String secondApproverName;
	private String secondApproverStatus;
	private Long thirdApproverId;
	private String thirdApproverName;
	private String thirdApproverStatus;

	public TaskProcessDTO() {
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(Long requesterId) {
		this.requesterId = requesterId;
	}

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getFirstApproverId() {
		return firstApproverId;
	}

	public void setFirstApproverId(Long firstApproverId) {
		this.firstApproverId = firstApproverId;
	}

	public String getFirstApproverName() {
		return firstApproverName;
	}

	public void setFirstApproverName(String firstApproverName) {
		this.firstApproverName = firstApproverName;
	}

	public String getFirstApproverStatus() {
		return firstApproverStatus;
	}

	public void setFirstApproverStatus(String firstApproverStatus) {
		this.firstApproverStatus = firstApproverStatus;
	}

	public Long getSecondApproverId() {
		return secondApproverId;
	}

	public void setSecondApproverId(Long secondApproverId) {
		this.secondApproverId = secondApproverId;
	}

	public String getSecondApproverName() {
		return secondApproverName;
	}

	public void setSecondApproverName(String secondApproverName) {
		this.secondApproverName = secondApproverName;
	}

	public String getSecondApproverStatus() {
		return secondApproverStatus;
	}

	public void setSecondApproverStatus(String secondApproverStatus) {
		this.secondApproverStatus = secondApproverStatus;
	}

	public Long getThirdApproverId() {
		return thirdApproverId;
	}

	public void setThirdApproverId(Long thirdApproverId) {
		this.thirdApproverId = thirdApproverId;
	}

	public String getThirdApproverName() {
		return thirdApproverName;
	}

	public void setThirdApproverName(String thirdApproverName) {
		this.thirdApproverName = thirdApproverName;
	}

	public String getThirdApproverStatus() {
		return thirdApproverStatus;
	}

	public void setThirdApproverStatus(String thirdApproverStatus) {
		this.thirdApproverStatus = thirdApproverStatus;
	}
}
