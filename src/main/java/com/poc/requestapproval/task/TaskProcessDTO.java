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
	private String status1;
	private long secondApproverId;
	private String secondApproverName;
	private String status2;
	private long thirdApproverId;
	private String thirdApproverName;
	private String status3;

	public TaskProcessDTO() {
	}

	public void setVariableData(Map<String, String> jsonVariableData) {
		switch (jsonVariableData.get(VARIABLE_ID)) {
			case REQUESTER_ID: {
				setRequesterId(Long.parseLong(jsonVariableData.get(VARIABLE_VALUE)));
			}break;
			case FIRST_APPROVER: {
				setFirstApproverId(Long.parseLong(jsonVariableData.get(VARIABLE_VALUE)));
			}break;
			case SECOND_APPROVER: {
				setSecondApproverId(Long.parseLong(jsonVariableData.get(VARIABLE_VALUE)));
			}break;
			case THIRD_APPROVER: {
				setThirdApproverId(Long.parseLong(jsonVariableData.get(VARIABLE_VALUE)));
			}break;
			case REQUESTER_NAME: {
				setRequesterName(jsonVariableData.get(VARIABLE_VALUE));
			}break;
			case FIRST_APPROVER_NAME: {
				setFirstApproverName(jsonVariableData.get(VARIABLE_VALUE));
			}break;
			case FIRST_APPROVER_STATUS: {
				setStatus1(jsonVariableData.get(VARIABLE_VALUE));
			}break;
			case SECOND_APPROVER_NAME: {
				setSecondApproverName(jsonVariableData.get(VARIABLE_VALUE));
			}break;
			case SECOND_APPROVER_STATUS: {
				setStatus2(jsonVariableData.get(VARIABLE_VALUE));
			}break;
			case THIRD_APPROVER_NAME: {
				setThirdApproverName(jsonVariableData.get(VARIABLE_VALUE));
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
