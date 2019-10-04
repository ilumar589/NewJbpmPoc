import alt from "../alt";
import axios from 'axios';
import HttpUtils from '../components/Common/HttpUtils';

class RequestsActions {

    fetchRequests(userId) {
        const requestOptions = HttpUtils.getDefaultOptions();
        requestOptions.url = requestOptions.baseURL + 'api/requests/' + userId;
        requestOptions.method = 'GET';

        axios(requestOptions)
            .then((response) => this.fetchRequestsComplete(response.data))
            .catch();

        return null;
    }

    fetchRequestsComplete(data) {
        // return data;
        let requests = [
            {processId: 1, requester: "Requester 1", approver1: "admin", status1: "Pending", approver2: "", status2: "", approver3: "", status3:"" },
            {processId: 2, requester: "Requester 1", approver1: "Sean Duncan", status1: "Approved", approver2: "Zion Bradley", status2: "Approved", approver3: "Will Jenkins", status3:"Declined" },
            {processId: 3, requester: "Requester 2", approver1: "John Braun", status1: "Approved", approver2: "Zion Bradley", status2: "Pending", approver3: "", status3:"" }];
        return requests;
    }

    createRequest(requestParams) {
	    const requestOptions = HttpUtils.getDefaultOptions();
	    requestOptions.url = requestOptions.baseURL + 'api/requests';
	    requestOptions.method = 'POST';
	    requestOptions.data = requestParams;

	    axios(requestOptions)
		    .then((response) => this.createRequestComplete(response.data))
		    .catch((err) => this.createRequestComplete);

	    return null;
    }

	createRequestComplete(data) {

    }

    completeRequest(taskId, requestParams) {
	    const requestOptions = HttpUtils.getDefaultOptions();
	    requestOptions.url = requestOptions.baseURL + 'api/requests/complete/' + taskId;
	    requestOptions.method = 'POST';
	    requestOptions.data = requestParams;

	    axios(requestOptions)
		    .then((response) => this.createRequestComplete(response.data))
		    .catch((err) => this.createRequestComplete);

	    return null;
    }

    startTaskRequest(taskId) {
	    const requestOptions = HttpUtils.getDefaultOptions();
	    requestOptions.url = requestOptions.baseURL + 'api/requests/start/' + taskId;
	    requestOptions.method = 'POST';

	    axios(requestOptions)
		    .then((response) => this.createRequestComplete(response.data))
		    .catch((err) => this.createRequestComplete);

	    return null;
    }
}

export default alt.createActions(RequestsActions);