import alt from "../alt";
import AccountActions from "../actions/AccountActions";

class AccountStore {
	constructor() {
		this.user = null;
		this.roles = [];
		this.isUserAuthenticated = false;
		this.userId = null;
		this.approvers = [];

		this.bindListeners({
			fetchAccount: AccountActions.fetchAccount,
			fetchAccountComplete: AccountActions.fetchAccountComplete,
			fetchUsersByRole: AccountActions.fetchUsersByRole,
			fetchUsersByRoleCompleted: AccountActions.fetchUsersByRoleCompleted
		});
	}

	fetchAccount() {
		this.user = null;
		this.roles = [];
	}

	fetchAccountComplete(data) {
		this.user = data.login;
		this.userId = data.id;
		this.roles = data.authorities;
		this.isUserAuthenticated = true;
	}

	fetchUsersByRole() {
		this.approvers = [];
	}

	fetchUsersByRoleCompleted(data) {
		this.approvers = data;
	}
}

export default alt.createStore(AccountStore , 'AccountStore');