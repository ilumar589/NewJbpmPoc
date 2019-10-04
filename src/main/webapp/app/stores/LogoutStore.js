import alt from "../alt";
import LogoutActions from "../actions/LogoutActions";

class LogoutStore {
	constructor() {
		this.user = null;
		this.roles = [];
		this.isUserAuthenticated = false;

		this.bindListeners({
			handleLogoutCompleted: LogoutActions.logoutComplete
		});
	}

	handleLogoutCompleted() {
		this.user = null;
		this.roles = null;
		this.isUserAuthenticated = false;
	}

}

export default alt.createStore(LogoutStore, 'LogoutStore');