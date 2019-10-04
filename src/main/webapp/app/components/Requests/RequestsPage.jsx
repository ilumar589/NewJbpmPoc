import React from 'react';
import {Modal} from "react-bootstrap";
import _ from "lodash";

import AssignApprover from "./AssignApprover";
import WithStoreSubscription from "../Common/hocs/WithStoreSubscriptions";
import RequestsStore from "../../stores/RequestsStore";
import RequestsActions from "../../actions/RequestsActions";
import LoginStore from "../../stores/LoginStore";
import LoginActions from "../../actions/LoginActions";
import AccountStore from "../../stores/AccountStore";
import AccountActions from "../../actions/AccountActions";
import CreateRequest from "./CreateRequest";

@WithStoreSubscription([AccountStore], [AccountActions.fetchAccount.defer])
class RequestsPage extends React.Component{

	constructor() {
		super();
		this.state = {
			requests: [],
			fetchingRequests: false
		};
	}

	componentDidMount() {
		RequestsStore.listen(this.onRequestStoreChanged);
		if(this.props.userId) {
			RequestsActions.fetchRequests.defer(this.props.userId);
		}
	}

	componentWillUnmount() {
		RequestsStore.unlisten(this.onRequestStoreChanged);
	}

	onRequestStoreChanged = store => {
		this.setState({...store});
		console.log(this.state);
	};

	onSubmitAssign() {
		event.preventDefault();
		this.setState({showAssignModal:true});
		RequestsActions.startTaskRequest(16);
	}

	onAssignCompleted = () => {
		this.setState({showAssignModal:false});
	};

	onSubmitCreate() {
		event.preventDefault();
		this.setState({showCreateModal:true});
	}

	onCreateCompleted() {
		this.setState({showCreateModal:false});
	}

	render() {

        const renderStatus = (request, number) => request["approver" + number] === this.props.user ? (request["status" + number] === "Pending" ?
	        <div className="btn-group-sm" role="group" >
                <button type="button" className="btn btn-primary" onClick={this.onSubmitAssign.bind(this)}>Approve</button>
                <button type="button" className="btn btn-secondary">Decline</button>
            </div>
	        : request["status" + number]) : request["status" + number];

		const renderRequests = _.map(this.state.requests, request => {
            return (
                <tr key={request.processId}>
                    <th scope="row">{request.processId}</th>
	                <th>{request.date || '10 Sept 2019'}</th>
	                <td>{request.requester}</td>
                    <td>{request.approver1 || '-'}</td>
                    <td>{renderStatus(request, 1) || '-'}</td>
                    <td>{request.approver2 || '-'}</td>
                    <td>{renderStatus(request, 2) || '-'}</td>
                    <td>{request.approver3 || '-'}</td>
                    <td>{renderStatus(request, 3) || '-'}</td>
                </tr>
            );
        });

		return (<React.Fragment>
			<h1>Requests</h1>

			{this.renderCreateModal()}
			{this.renderAssignModal()}
			{this.renderCreateButton()}
			<div className="standard-container">
				<table className="table table-striped">
					<thead>
					<tr>
						<th scope="col">PID</th>
						<th scope="col">Date</th>
						<th scope="col">Requester</th>
						<th scope="col">First Approver</th>
						<th scope="col">Status</th>
						<th scope="col">Second Approver</th>
						<th scope="col">Status</th>
						<th scope="col">Third Approver</th>
						<th scope="col">Status</th>
					</tr>
					</thead>
					<tbody>
						{renderRequests}
					</tbody>
				</table>
			</div>
			</React.Fragment>);
	}

	renderAssignModal = () => {
		return (
			<Modal show={this.state.showAssignModal} onHide={() => this.onAssignCompleted()}>
				<Modal.Header closeButton>
					<Modal.Title>Assign next approver</Modal.Title>
				</Modal.Header>
				<Modal.Body>
					<AssignApprover onAssignCompleted={() => this.onAssignCompleted()} taskId={this.state.taskId}></AssignApprover>
				</Modal.Body>
			</Modal>
		);
	};

	renderCreateModal = () => {
		return (
			<Modal show={this.state.showCreateModal} onHide={() => this.onCreateCompleted()}>
				<Modal.Header closeButton>
					<Modal.Title>Assign approver</Modal.Title>
				</Modal.Header>
				<Modal.Body>
					<CreateRequest onCreateCompleted={() => this.onCreateCompleted()} requesterId={this.props.userId}></CreateRequest>
				</Modal.Body>
			</Modal>
		);
	};

	renderCreateButton = () => {
		return( this.props.roles ? (
			this.props.roles.indexOf("REQUESTER_0") >= 0 ?
					<input type="submit" className="float-right" value="Create Request" onClick={this.onSubmitCreate.bind(this)}/>
				: null ) : null
		);
	}
}


export default RequestsPage;