package com.poc.requestapproval.web.rest;


import com.poc.requestapproval.jbpm.JbpmService;
import com.poc.requestapproval.service.util.Page;
import com.poc.requestapproval.task.RequestFilter;
import com.poc.requestapproval.task.RequestsStatistics;
import com.poc.requestapproval.task.TaskProcessDTO;
import com.poc.requestapproval.task.TaskRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.poc.requestapproval.service.util.Page.Builder.page;

@RestController
@RequestMapping("/api")
public class RequestResource {

	private final JbpmService jbpmService;

	public RequestResource(JbpmService jbpmService) {
		this.jbpmService = jbpmService;
	}

	@PostMapping("/requests/{userId}")
	public Page<TaskProcessDTO> getApprovalDataForLoggedInUser(@PathVariable Long userId,
	                                                           @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
	                                                           @RequestParam(value = "size", required = false, defaultValue = "5") Integer size,
	                                                           @RequestBody RequestFilter requestFilter) {

		List<TaskProcessDTO> approvalData = jbpmService.getFilteredApprovalDataForLoggedInUser(requestFilter);

		return page(TaskProcessDTO.class)
				.withCurrentPage(jbpmService.getCurrentPage(approvalData, size, page))
				.withLimit(size)
				.withOffset(page*size)
				.withTotalCount((long) approvalData.size())
				.build();
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
	public void startTask(@PathVariable Long taskId) {
		jbpmService.start(taskId);
	}

	@PostMapping("/requests/decline/{taskId}")
	public void declineRequest(@PathVariable Long taskId, @RequestBody Map<String, String> params) {
		jbpmService.complete(taskId, params);
	}

	@PostMapping("/requests/statistics")
	public RequestsStatistics getRequestStatistics(@RequestBody(required = false) RequestFilter requestFilter) {
		return jbpmService.getRequestsStatistics(requestFilter);
	}

}
