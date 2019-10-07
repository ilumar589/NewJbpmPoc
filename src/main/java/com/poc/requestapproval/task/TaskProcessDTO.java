package com.poc.requestapproval.task;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import static com.poc.requestapproval.jbpm.JbpmService.*;

public class TaskProcessDTO implements Serializable {

	private int processInstanceId;
	private long taskId;
	private String taskStatus;
	private long requesterId;
	private String requesterName;
	private String date;
	private long approver1;
	private String approver1Name;
	private String status1;
	private long approver2;
	private String approver2Name;
	private String status2;
	private long approver3;
	private String approver3Name;
	private String status3;

	public TaskProcessDTO() {
	}

	public void setVariableData(Map<String, String> jsonVariableData) {
		switch (jsonVariableData.get(VARIABLE_ID)) {
			case REQUESTER_ID: {
				setRequesterId(Long.parseLong(jsonVariableData.get(VARIABLE_VALUE)));
			}break;
			case FIRST_APPROVER: {
				setApprover1(Long.parseLong(jsonVariableData.get(VARIABLE_VALUE)));
			}break;
			case SECOND_APPROVER: {
				setApprover2(Long.parseLong(jsonVariableData.get(VARIABLE_VALUE)));
			}break;
			case THIRD_APPROVER: {
				setApprover3(Long.parseLong(jsonVariableData.get(VARIABLE_VALUE)));
			}break;
			case REQUESTER_NAME: {
				setRequesterName(jsonVariableData.get(VARIABLE_VALUE));
			}break;
			case FIRST_APPROVER_NAME: {
				setApprover1Name(jsonVariableData.get(VARIABLE_VALUE));
			}break;
			case FIRST_APPROVER_STATUS: {
				setStatus1(jsonVariableData.get(VARIABLE_VALUE));
			}break;
			case SECOND_APPROVER_NAME: {
				setApprover2Name(jsonVariableData.get(VARIABLE_VALUE));
			}break;
			case SECOND_APPROVER_STATUS: {
				setStatus2(jsonVariableData.get(VARIABLE_VALUE));
			}break;
			case THIRD_APPROVER_NAME: {
				setApprover3Name(jsonVariableData.get(VARIABLE_VALUE));
			}break;
			case THIRD_APPROVER_STATUS: {
				setStatus3(jsonVariableData.get(VARIABLE_VALUE));
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

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getApprover1() {
		return approver1;
	}

	public void setApprover1(long approver1) {
		this.approver1 = approver1;
	}

	public String getApprover1Name() {
		return approver1Name;
	}

	public void setApprover1Name(String approver1Name) {
		this.approver1Name = approver1Name;
	}

	public long getApprover2() {
		return approver2;
	}

	public void setApprover2(long approver2) {
		this.approver2 = approver2;
	}

	public String getApprover2Name() {
		return approver2Name;
	}

	public void setApprover2Name(String approver2Name) {
		this.approver2Name = approver2Name;
	}

	public long getApprover3() {
		return approver3;
	}

	public void setApprover3(long approver3) {
		this.approver3 = approver3;
	}

	public String getApprover3Name() {
		return approver3Name;
	}

	public void setApprover3Name(String approver3Name) {
		this.approver3Name = approver3Name;
	}

	public String getStatus1() {
		return status1;
	}

	public void setStatus1(String status1) {
		this.status1 = status1;
	}

	public String getStatus2() {
		return status2;
	}

	public void setStatus2(String status2) {
		this.status2 = status2;
	}

	public String getStatus3() {
		return status3;
	}

	public void setStatus3(String status3) {
		this.status3 = status3;
	}
}
