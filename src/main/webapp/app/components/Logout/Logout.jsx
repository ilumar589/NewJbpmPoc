import React from 'react';
import LogoutActions from "../../actions/LogoutActions";
import LogoutStore from "../../stores/LogoutStore";
import WithStoreSubscription from "../Common/hocs/WithStoreSubscriptions";

@WithStoreSubscription([LogoutStore])
class Logout extends React.Component {


    constructor() {
        super();
        this.state = {

        };
    }

    onSubmit() {
        event.preventDefault();
        LogoutActions.logout();
    }

    render() {
        return <button className="btn btn-primary float-right" type="submit" onClick={()=> this.onSubmit()}>Logout</button>;
    }
}

export default Logout;