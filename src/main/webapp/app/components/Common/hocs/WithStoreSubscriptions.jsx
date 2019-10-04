import React from 'react';
import _ from 'lodash';

const WithStoreSubscription = (stores, onMount) => WrappedComponent => {
	class hoc extends React.Component {

		constructor() {
			super();
			this.state = this.getInitialState(stores);
		}

		getInitialState(stores) { //initial store ul nu are state si pe constructor faci merge intre state uri
			var tempState = {};

			_.each(stores, store=>_.merge(tempState, store.getState()));
			return tempState;
		}

		componentDidMount() {
			_.each(stores, store => {
				store.listen(this.onStoreUpdated);
			});
			_.each(onMount, action => {
				action();
			});
		}

		componentWillUnmount() {
			_.each(stores, store => {
				store.unlisten(this.onStoreUpdated);
			});
		}

		onStoreUpdated = (store) => {
			this.setState({...store});
		};

		render() {
			return <WrappedComponent {...this.state} {...this.props}></WrappedComponent>;
		}
	};

	return hoc;
};

export default WithStoreSubscription;


