import alt from "../alt";
import LoginActions from "../actions/LoginActions";

class LoginStore {
	constructor() {
		this.isUserAuthenticated = false;

		this.bindListeners({
			handleLogin: LoginActions.login,
			handleLoginCompleted: LoginActions.loginComplete
		});
	}

	handleLogin() {
		this.isUserAuthenticated = false;
	}

	handleLoginCompleted(data) {
		this.isUserAuthenticated = true;
		window.localStorage.setItem("isUserAuthenticated", true);
	}

}

export default alt.createStore(LoginStore, 'LoginStore');