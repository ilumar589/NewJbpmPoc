package com.poc.requestapproval.jbpm;

import com.poc.requestapproval.domain.Authority;
import com.poc.requestapproval.domain.User;
import com.poc.requestapproval.domain.UserAuthorityType;
import com.poc.requestapproval.service.UserService;
import com.poc.requestapproval.task.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Strings.isNullOrEmpty;

@Service
public class JbpmService {

	private static final String JBPM_REQUESTER_VAR = "requesterId";
	private static final String JBPM_FIRST_APPROVER_VAR = "firstApprover";
	private static final String JBPM_SECOND_APPROVER_VAR = "secondApprover";
	private static final String JBPM_THIRD_APPROVER_VAR = "thirdApprover";

	private static final Map<String, String> roleIndexToJbpmVariableMapping;

	static {
		roleIndexToJbpmVariableMapping = new HashMap<>(4);
		roleIndexToJbpmVariableMapping.put("0", JBPM_REQUESTER_VAR);
		roleIndexToJbpmVariableMapping.put("1", JBPM_FIRST_APPROVER_VAR);
		roleIndexToJbpmVariableMapping.put("2", JBPM_SECOND_APPROVER_VAR);
		roleIndexToJbpmVariableMapping.put("3", JBPM_THIRD_APPROVER_VAR);
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

    private List<TaskDto> getTasksByPid(long pid) {
    	String url = JBPM_REST_URL + "/task/query";

	    UriComponentsBuilder builder = UriComponentsBuilder
			    .fromHttpUrl(url)
			    .queryParam("processInstanceId", pid);

		ResponseEntity<TaskSummaryWrapper> response =
				authenticatedRestTemplate.getForEntity(builder.toUriString(), TaskSummaryWrapper.class);

		if (response.hasBody()
				&& response.getBody() != null
				&& response.getBody().getTaskSummaries() != null) {
			return response.getBody().getTaskSummaries();
		}

		return Collections.emptyList();
    }

    private Object getProcessVariables(long pid) {
		ResponseEntity<String> responseEntity =
				authenticatedRestTemplate.getForEntity(JBPM_REST_URL + "/runtime/" + DEPLOYMENT_ID + "process/instance/" + pid + "/variables", String.class);

		System.out.println(responseEntity.getBody());

		return responseEntity.getBody();
    }

    public Collection<TaskProcessDTO> getApprovalDataForLoggedInUser() {
	    List<ProcessDTO> processes = getProcessesForLoggedInUser();
	    List<TaskProcessDTO> approvalData = new ArrayList<>(processes.size());

	    for (ProcessDTO process : processes) {
	    	List<TaskDto> associatedTasks = getTasksByPid(process.getPid());
	    	Object associatedProcessVariables = getProcessVariables(process.getPid());
	    }

	    //append map_taskOwner
//	    ResponseEntity<TaskSummaryWrapper> response = authenticatedRestTemplate.getForEntity(JBPM_REST_URL + "/rest/task/query", TaskSummaryWrapper.class);

	    //todo
	    // /runtime/{deploymentId}/history/instance/{procInstId}/variable
	    //return as Collection<TaskProcessDTO>

		return null;
	}

	public void start(Long taskId) {
        authenticatedRestTemplate.postForEntity(JBPM_REST_URL + "/task/" + taskId + "/start",  null, null);
	}

	public void complete(Long taskId, Map<String, String> params) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

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

	private List<ProcessDTO> getProcessesForLoggedInUser() {
		Optional<User> user = userService.getUserWithAuthorities();
		if(user.isPresent()) {
			// in case we have the same user mapped to 2 or more approval roles in separate processes
			List<ProcessDTO> processListForAllApprovalRoles = new ArrayList<>();

			// only extract approval roles
			List<Authority> authorityList = user.get().getAuthorities()
					.stream()
					.filter(role -> !role.getName().equals(UserAuthorityType.ADMIN))
					.collect(Collectors.toList());

			for (Authority authority : authorityList) {
				String processVar = roleIndexToJbpmVariableMapping.get(authority.getName().toString().split("_")[1]);

				ResponseEntity<ProcessSummaryWrapper> response = authenticatedRestTemplate
						.getForEntity(JBPM_REST_URL + "/history/variable/" + processVar + "/value/" + user.get().getId() + "/instances", ProcessSummaryWrapper.class);

				if (response.hasBody()
						&& response.getBody() != null
						&& response.getBody().getProcessSummaries() != null) {
					processListForAllApprovalRoles.addAll(response.getBody().getProcessSummaries());
				}
			}

			return processListForAllApprovalRoles;
		}
		return Collections.emptyList();
	}
}
