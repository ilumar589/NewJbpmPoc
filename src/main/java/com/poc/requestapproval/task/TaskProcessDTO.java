package com.poc.requestapproval.task;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import static com.poc.requestapproval.jbpm.JbpmService.*;

public class TaskProcessDTO implements Serializable {

	private int processInstanceId;
	private long taskId;
	private long requesterId;
	private String requesterName;
	private Date date;
	private long firstApproverId;
	private String firstApproverName;
	private String firstApproverStatus;
	private long secondApproverId;
	private String secondApproverName;
	private String secondApproverStatus;
	private long thirdApproverId;
	private String thirdApproverName;
	private String thirdApproverStatus;

	public TaskProcessDTO() {
	}

	public void setVariableData(Map<String, String> jsonVariableData) {
		switch (jsonVariableData.get(VARIABLE_ID)) {
			case REQUESTER_ID: {

			}break;
			case FIRST_APPROVER: {

			}break;
			case SECOND_APPROVER: {

			}break;
			case THIRD_APPROVER: {

			}break;
		}

	}

	public int getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(int processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public long getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(long requesterId) {
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

	public long getFirstApproverId() {
		return firstApproverId;
	}

	public void setFirstApproverId(long firstApproverId) {
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

	public long getSecondApproverId() {
		return secondApproverId;
	}

	public void setSecondApproverId(long secondApproverId) {
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

	public long getThirdApproverId() {
		return thirdApproverId;
	}

	public void setThirdApproverId(long thirdApproverId) {
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
