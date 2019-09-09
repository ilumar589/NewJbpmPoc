package com.poc.requestapproval.jbpm;

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

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JbpmService {

	private static String DEPLOYMENT_ID = "com.requestapproval:request-approval:LATEST";
	private static String PROCESS_ID = "ApprovalRequest";
	private String jbpmConsoleUrl = "http://localhost:8180/jbpm-console";

	public Collection<TaskDto> query() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor("admin", "admin"));
		ResponseEntity<TaskSummaryWrapper> response = restTemplate.getForEntity(jbpmConsoleUrl + "/rest/task/query", TaskSummaryWrapper.class);
		return response.getBody().getTasks();
	}

	public void start(Long taskId) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor("admin", "admin"));
		restTemplate.postForEntity(jbpmConsoleUrl + "/rest/task/" + taskId + "/start",  null, null);
	}

	public void complete(Long taskId, Map<String, String> params) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor("admin", "admin"));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			map.add("map_" + entry.getKey(), entry.getValue().toString());
		}
		restTemplate.postForEntity(jbpmConsoleUrl + "/rest/task/" + taskId + "/complete",  map, null);
	}

	public void startProcess(TaskRequest taskRequest) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor("admin", "admin"));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("map_requesterId", taskRequest.getRequesterId().toString());
		map.add("map_firstApprover", taskRequest.getFirstApprover().toString());
		map.add("map_date", String.valueOf(new Date()));

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

		restTemplate.postForEntity(jbpmConsoleUrl + "/rest/runtime/" + DEPLOYMENT_ID + "/process/" + PROCESS_ID + "/start",  request, null);
	}

	}
