import alt from '../alt';
import RequestsActions from "../actions/RequestsActions";

class RequestsStore {
    constructor() {
        this.requests = [];
        this.fetchingRequests = false;

        this.bindListeners({
            handleFetchRequests: RequestsActions.fetchRequests,
            handleFetchRequestsComplete: RequestsActions.fetchRequestsComplete
        });
    }

    handleFetchRequests() {
        this.fetchingRequests = true;
        this.requests = [];
    }

    handleFetchRequestsComplete(requests) {
        this.fetchingRequests = false;
        this.requests = requests;
    }
}

export default alt.createStore(RequestsStore, 'RequestsStore');