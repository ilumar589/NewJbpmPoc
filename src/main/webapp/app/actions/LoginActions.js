import alt from "../alt";
import axios from 'axios';
import HttpUtils from '../components/Common/HttpUtils';

class LoginActions {

	login(requestParams) {
		const requestOptions = HttpUtils.getDefaultOptions();
		requestOptions.url = requestOptions.baseURL + 'api/authentication';
		requestOptions.method = 'POST';
		requestOptions.headers= { 'Content-Type': 'application/x-www-form-urlencoded' };
		requestOptions.data = 'username=' + requestParams.username + '&password=' + requestParams.password + '&remember-me=true&submit=Login';

		axios(requestOptions)
			.then((response) => this.loginComplete(response.data))
			.catch((err) => console.log(err));

		return null;
	}

	loginComplete(data) {
		return data;
	}
}

export default alt.createActions(LoginActions);