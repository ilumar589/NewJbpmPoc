package com.poc.requestapproval.jbpm;

import com.poc.requestapproval.domain.Authority;
import com.poc.requestapproval.domain.User;
import com.poc.requestapproval.service.UserService;
import com.poc.requestapproval.task.TaskDto;
import com.poc.requestapproval.task.TaskRequest;
import com.poc.requestapproval.task.TaskSummaryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class JbpmService {

	private static final String DEPLOYMENT_ID = "com.requestapproval:request-approval:LATEST";
	private static final String PROCESS_ID = "ApprovalRequest";
	private static final String JBPM_CONSOLE_URL = "http://localhost:8180/jbpm-console";

	private final UserService userService;
	private final RestTemplate authenticatedRestTemplate;

    public JbpmService(UserService userService, RestTemplate authenticatedRestTemplate) {
        this.userService = userService;
        this.authenticatedRestTemplate = authenticatedRestTemplate;
    }


    public Collection<TaskDto> query() {
		//append map_taskOwner
		ResponseEntity<TaskSummaryWrapper> response = authenticatedRestTemplate.getForEntity(JBPM_CONSOLE_URL + "/rest/task/query", TaskSummaryWrapper.class);

		//todo
		// /runtime/{deploymentId}/history/instance/{procInstId}/variable
		//return as Collection<TaskProcessDTO>
		String processes = getProcesses();

		return Objects.requireNonNull(response.getBody()).getTasks();
	}

	public void start(Long taskId) {
        authenticatedRestTemplate.postForEntity(JBPM_CONSOLE_URL + "/rest/task/" + taskId + "/start",  null, null);
	}

	public void complete(Long taskId, Map<String, String> params) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			map.add("map_" + entry.getKey(), entry.getValue());
		}
        authenticatedRestTemplate.postForEntity(JBPM_CONSOLE_URL + "/rest/task/" + taskId + "/complete",  map, null);
	}

	public void startProcess(TaskRequest taskRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("map_requesterId", taskRequest.getRequesterId().toString());
		map.add("map_firstApprover", taskRequest.getFirstApprover().toString());
		map.add("map_date", String.valueOf(new Date()));

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        authenticatedRestTemplate.postForEntity(JBPM_CONSOLE_URL + "/rest/runtime/" + DEPLOYMENT_ID + "/process/" + PROCESS_ID + "/start",  request, null);
	}

	public String getProcesses() {
		Optional<User> user = userService.getUserWithAuthorities();
		if(user.isPresent()) {
			Authority authority = userService.getRequesterOrApproverRole(user);
			String processVar = getProcessVariable(authority.getName().split("_")[0]);

			ResponseEntity<String> response =
                authenticatedRestTemplate.getForEntity(JBPM_CONSOLE_URL + "/rest/history/variable/" + processVar + "/value/" + user.get().getId() + "/instances", String.class);
			return response.getBody();
		}
		return null;
	}

	private String getProcessVariable(String index) {
		String processVar = "";
		switch (index) {
			case "0" :
				processVar = "requesterId";
				break;
			case "1" :
				processVar = "firstApprover";
				break;
			case "2" :
				processVar = "secondApprover";
				break;
			case "3" :
				processVar = "thirdApprover";
				break;
		}
		return processVar;
	}

	}
