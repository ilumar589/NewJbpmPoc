package com.poc.requestapproval.jbpm;

import com.jayway.jsonpath.JsonPath;
import com.poc.requestapproval.domain.Authority;
import com.poc.requestapproval.domain.User;
import com.poc.requestapproval.domain.UserAuthorityType;
import com.poc.requestapproval.service.UserService;
import com.poc.requestapproval.task.TaskDto;
import com.poc.requestapproval.task.TaskProcessDTO;
import com.poc.requestapproval.task.TaskRequest;
import com.poc.requestapproval.task.TaskSummaryWrapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JbpmService {
	private static final String PID            = "process-instance-id";
	private static final String START_DATE     = "start";
	private static final String PROCESS_STATUS = "status";
	public static final String VARIABLE_ID    = "variable-id";
	public static final String VARIABLE_VALUE = "value";

	public static final String REQUESTER_ID           = "requesterId";
	public static final String REQUESTER_NAME         = "requesterName";
	public static final String FIRST_APPROVER         = "firstApprover";
	public static final String FIRST_APPROVER_NAME    = "firstApproverName";
	public static final String FIRST_APPROVER_STATUS  = "status1";
	public static final String SECOND_APPROVER        = "secondApprover";
	public static final String SECOND_APPROVER_NAME   = "secondApproverName";
	public static final String SECOND_APPROVER_STATUS = "status2";
	public static final String THIRD_APPROVER         = "thirdApprover";
	public static final String THIRD_APPROVER_NAME    = "thirdApproverName";
	public static final String THIRD_APPROVER_STATUS  = "status3";

	private static final Map<String, String> roleIndexToJbpmVariableMapping;

	static {
		roleIndexToJbpmVariableMapping = new HashMap<>(4);
		roleIndexToJbpmVariableMapping.put("0", REQUESTER_ID);
		roleIndexToJbpmVariableMapping.put("1", FIRST_APPROVER);
		roleIndexToJbpmVariableMapping.put("2", SECOND_APPROVER);
		roleIndexToJbpmVariableMapping.put("3", THIRD_APPROVER);
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
	    Optional<User> optionalUser         = userService.getUserWithAuthorities();
	    if (!optionalUser.isPresent()) {
		    throw new RuntimeException("No logged in user!");
	    }

	    long userId = optionalUser.get().getId();

	    List<Map<String, Object>> processes = getProcessesForLoggedInUser();
	    List<TaskProcessDTO> approvalData   = new ArrayList<>(processes.size());

	    for (Map<String, Object> process : processes) {
	    	int pid = (int) process.get(PID);
	    	Optional<TaskDto> associatedTask = getTaskByPidAndOwner(pid, userId);
	    	Collection<Map<String, String>> associatedProcessVariables = getProcessVariables(pid);

	    	//----- Create Approval data -----//
	    	TaskProcessDTO approvalObject = new TaskProcessDTO();
	    	approvalObject.setProcessInstanceId(pid);
		    String pattern = "MM-dd-yyyy";
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		    approvalObject.setDate(simpleDateFormat.format(new Date((long) process.get(START_DATE))));

	    	//--- Process variable data ----//
		    for (Map<String, String> variable : associatedProcessVariables) {
		    	approvalObject.setVariableData(variable);
		    }
		    enrichWithUserDetails(approvalObject);

		    //--- Task Data ---//
		    associatedTask.ifPresent(task -> approvalObject.setTaskId(task.getId()));

	    	approvalData.add(approvalObject);
	    }

		return approvalData.stream().sorted((a1, a2) -> Long.compare(a2.getProcessInstanceId(), a1.getProcessInstanceId())).collect(Collectors.toList());
	}

	private void enrichWithUserDetails(TaskProcessDTO approvalObject) {
		User requester = userService.getUser(approvalObject.getRequesterId());
		approvalObject.setRequesterName(requester.getFirstName() + " " + requester.getLastName());

		if(approvalObject.getApprover1() != 0) {
			User firstApprover = userService.getUser(approvalObject.getApprover1());
			approvalObject.setApprover1Name(firstApprover.getFirstName() + " " + firstApprover.getLastName());
		}
		if(approvalObject.getApprover2() != 0) {
			User secondApprover = userService.getUser(approvalObject.getApprover2());
			approvalObject.setApprover2Name(secondApprover.getFirstName() + " " + secondApprover.getLastName());
		}
		if(approvalObject.getApprover3() != 0) {
			User thirdApprover = userService.getUser(approvalObject.getApprover3());
			approvalObject.setApprover3Name(thirdApprover.getFirstName() + " " + thirdApprover.getLastName());
		}
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

	private Collection<Map<String, String>> getProcessVariables(int pid) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response =
				authenticatedRestTemplate.exchange(JBPM_REST_URL + "/history/instance/" + pid + "/variable",
						HttpMethod.GET,
						entity,
						String.class);

		return JsonPath.read(response.getBody(), "$.historyLogList[*].*");
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
