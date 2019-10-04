export default class HttpUtils {
	/**
	 * Gets the default option values for http requests
	 */
	static getDefaultOptions() {
		return {
			baseURL: '',
			// headers: {
			// 	'Content-Type': 'application/json; charset=utf-8',
			// 	'Accept': 'application/json, text/plain, */*'
			// },
			method: 'GET'
		};
	}
}