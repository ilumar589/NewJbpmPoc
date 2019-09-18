package com.poc.requestapproval.web.rest;


import com.poc.requestapproval.jbpm.JbpmService;
import com.poc.requestapproval.task.TaskDto;
import com.poc.requestapproval.task.TaskProcessDTO;
import com.poc.requestapproval.task.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RequestResource {

	private final JbpmService jbpmService;

	public RequestResource(JbpmService jbpmService) {
		this.jbpmService = jbpmService;
	}

	@GetMapping("/requests/{userId}")
	public Collection<TaskProcessDTO> getApprovalDataForLoggedInUser(@PathVariable Long userId) {
		return jbpmService.getApprovalDataForLoggedInUser();
	}

	@PostMapping("/requests")
	public void createRequest(@RequestBody TaskRequest taskRequest) {
		jbpmService.startProcess(taskRequest);
	}

	@PostMapping("/requests/complete/{taskId}")
	public void completeRequest(@PathVariable Long taskId,
								@RequestBody Map<String, String> taskRequest) {
		jbpmService.complete(taskId, taskRequest);
	}

	@PostMapping("/requests/start/{taskId}")
	public void completeRequest(@PathVariable Long taskId) {
		jbpmService.start(taskId);
	}



}
