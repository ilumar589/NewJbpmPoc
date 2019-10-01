package com.poc.requestapproval.jbpm;

import com.jayway.jsonpath.JsonPath;
import com.poc.requestapproval.domain.Authority;
import com.poc.requestapproval.domain.User;
import com.poc.requestapproval.domain.UserAuthorityType;
import com.poc.requestapproval.service.UserService;
import com.poc.requestapproval.task.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JbpmService {
	private static final String PID            = "process-instance-id";
	private static final String START_DATE     = "start";
	private static final String PROCESS_STATUS = "status";
	private static final String VARIABLE_ID    = "variable-id";
	private static final String VARIABLE_VALUE = "value";

	private static final List<String> variableList;

	private static final Map<String, String> roleIndexToJbpmVariableMapping;

	static {
		variableList = new ArrayList<>(4);
		variableList.add("requesterId");
		variableList.add("firstApprover");
		variableList.add("secondApprover");
		variableList.add("thirdApprover");

		roleIndexToJbpmVariableMapping = new HashMap<>(4);
		roleIndexToJbpmVariableMapping.put("0", variableList.get(0));
		roleIndexToJbpmVariableMapping.put("1", variableList.get(1));
		roleIndexToJbpmVariableMapping.put("2", variableList.get(2));
		roleIndexToJbpmVariableMapping.put("3", variableList.get(3));
	}

	private static final String DEPLOYMENT_ID = "com.requestapproval:request-approval:LATEST";
	private static final String PROCESS_ID = "ApprovalRequest";
	private static final String JBPM_REST_URL = "http://localhost:8180/jbpm-console/rest";

	private final UserService userService;
	private final RestTemplate authenticatedRestTemplate;

    public JbpmService(UserService userService, RestTemplate authenticatedRestTemplate) {
        this.userService = userService;
        this.authenticatedRestTemplate = authenticatedRestTemplate;
    }

    public Collection<TaskProcessDTO> getApprovalDataForLoggedInUser() {
	    List<Map<String, Object>> processes = getProcessesForLoggedInUser();
	    List<TaskProcessDTO> approvalData   = new ArrayList<>(processes.size());
		Optional<User> optionalUser         = userService.getUserWithAuthorities();

		if (!optionalUser.isPresent()) {
			throw new RuntimeException("No logged in user!");
		}

		long userId = optionalUser.get().getId();
	    for (Map<String, Object> process : processes) {
	    	int pid = (int) process.get(PID);
	    	Optional<TaskDto> associatedTask = getTaskByPidAndOwner(pid, userId);
	    	Collection<Map<String, String>> associatedProcessVariables = getFilteredProcessVariables(pid);

	    	//----- Create Approval data -----//
	    	TaskProcessDTO approvalObject = new TaskProcessDTO();
	    	approvalObject.setProcessInstanceId(pid);
	    	associatedTask.ifPresent(task -> approvalObject.setTaskId(task.getId()));
	    }

		return approvalData;
	}

	public void start(Long taskId) {
        authenticatedRestTemplate.postForEntity(JBPM_REST_URL + "/task/" + taskId + "/start",  null, null);
	}

	public void complete(Long taskId, Map<String, String> params) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			map.add("map_" + entry.getKey(), entry.getValue());
		}
        authenticatedRestTemplate.postForEntity(JBPM_REST_URL + "/task/" + taskId + "/complete",  map, null);
	}

	public void startProcess(TaskRequest taskRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("map_requesterId", taskRequest.getRequesterId().toString());
		map.add("map_firstApprover", taskRequest.getFirstApprover().toString());
		map.add("map_date", String.valueOf(new Date()));

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        authenticatedRestTemplate.postForEntity(JBPM_REST_URL + "/runtime/" + DEPLOYMENT_ID + "/process/" + PROCESS_ID + "/start",  request, null);
	}

	private Optional<TaskDto> getTaskByPidAndOwner(long pid, long userId) {
		String url = JBPM_REST_URL + "/task/query";

		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(url)
				.queryParam("processInstanceId", pid)
				.queryParam("taskOwner", userId);

		ResponseEntity<TaskSummaryWrapper> response =
				authenticatedRestTemplate.getForEntity(builder.toUriString(), TaskSummaryWrapper.class);

		if (response.hasBody() && response.getBody() != null) {
			return response.getBody().getTaskSummaries().stream().findFirst();
		}

		return Optional.empty();
	}

	private Collection<Map<String, String>> getFilteredProcessVariables(int pid) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response =
				authenticatedRestTemplate.exchange(JBPM_REST_URL + "/history/instance/" + pid + "/variable",
						HttpMethod.GET,
						entity,
						String.class);

		Collection<Map<String, String>> jsonListToBeProcessed = JsonPath.read(response.getBody(), "$.historyLogList[*].*");

		return jsonListToBeProcessed.stream().filter(variable -> variableList.contains(variable.get(VARIABLE_ID))).collect(Collectors.toList());
	}

	private List<Map<String, Object>> getProcessesForLoggedInUser() {
		Optional<User> user = userService.getUserWithAuthorities();
		if(user.isPresent()) {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			HttpEntity<String> entity = new HttpEntity<>(null, headers);

			// in case we have the same user mapped to 2 or more approval roles in separate processes
			List<Map<String, Object>> processListForAllApprovalRoles = new ArrayList<>();

			// only extract approval roles
			List<Authority> authorityList = user.get().getAuthorities()
					.stream()
					.filter(role -> !role.getName().equals(UserAuthorityType.ADMIN))
					.collect(Collectors.toList());

			for (Authority authority : authorityList) {
				String processVar = roleIndexToJbpmVariableMapping.get(authority.getName().toString().split("_")[1]);

				// JaxbProcessInstanceListResponse
				ResponseEntity<String> response = authenticatedRestTemplate
						.exchange(JBPM_REST_URL + "/history/variable/" + processVar + "/value/" + user.get().getId() + "/instances",
								HttpMethod.GET,
								entity,
								String.class);

				processListForAllApprovalRoles.addAll(JsonPath.read(response.getBody(), "$.historyLogList[*].*"));
			}

			return processListForAllApprovalRoles;
		}
		return Collections.emptyList();
	}
}
