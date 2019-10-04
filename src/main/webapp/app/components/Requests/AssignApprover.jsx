import React from 'react';
import RequestsActions from "../../actions/RequestsActions";
import WithStoreSubscription from "../Common/hocs/WithStoreSubscriptions";
import AccountActions from "../../actions/AccountActions";
import AccountStore from "../../stores/AccountStore";

@WithStoreSubscription([AccountStore], [AccountActions.fetchUsersByRole.defer])
class AssignApprover extends React.Component {

	constructor() {
		super();
		this.state = {
			formChanged: false,
			approvers: []
		};
	}

	onSubmit() {
		event.preventDefault();
		RequestsActions.completeRequest(
			16, //todo
			{
			secondApprover: this.state.approver, //todo
		});
		this.props.onAssignCompleted();
	}

	inputChanged = (value) => {
		this.setState({...value, formChanged: true});
	};

	render() {
		return(
			<div>
				<form onSubmit={this.onSubmit.bind(this)}>
					<div className="form-group">
						<label htmlFor="approver">Approver</label>
						<select name="" id="approver" className="form-control" required onChange={(event) => {
							this.inputChanged({approver: event.target.value});}}>
							<option>Select Approver</option>
							{this.props.approvers.map((approver) =>
								<option key={approver.id} value={approver.id}>
									{approver.firstName} {approver.lastName}
								</option>
							)}
						</select>
					</div>
					<button type="submit" className="btn btn-primary" disabled={!this.state.formChanged}>Complete</button>
				</form>
			</div>
		);
	}
}

export default AssignApprover;