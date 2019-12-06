import React from 'react';
class Invite extends React.Component {

    constructor(props){
        super(props);
        this.state = {
            invitedPlayer: "",
            apiConfig:
            this.props.apiConfig,
            inviteSuccess: null
        };
        this.handleInvitedPlayerChange = this.handleInvitedPlayerChange.bind(this);
        this.handleGeneralRequest = this.handleGeneralRequest.bind(this);
        this.handleAcceptInvite = this.handleAcceptInvite.bind(this);
        this.handleDeclineInvite = this.handleDeclineInvite.bind(this);
    }

    render() {
        console.log(this.props.invites);
        if(typeof this.props.invites !== "undefined"){    // gotta be a better way of doing this...
            var playerInvites = this.props.invites.split(",");
            playerInvites.pop();
            console.log(playerInvites);
            var inviteButtons = playerInvites.map((elem) => <div style={{display:'block'}}>{elem}<button onClick={
                this.handleAcceptInvite.bind(this, elem)}>Accept</button>
                <button onClick={this.handleDeclineInvite.bind(this, elem)}>Decline</button></div>);
            console.log(inviteButtons);
        }

        let successMessage;
        if (this.state.inviteSuccess === false)
        {
            successMessage = <p style={{color:"red"}}>Invite was unsuccessful!</p>
        }
        else if (this.state.inviteSuccess === true)
        {
            successMessage = <p style={{color:"green"}}>Invite was successful!</p>
        }

        return (
            <div className={'InvitePage'}>
                <form onSubmit={(e) => this.handleGeneralRequest(e, this.state.apiConfig.url,
                    "action=SendInvite&playerOne=" + this.props.username + "&username=" + this.props.username + "&password=" + this.props.password + "&playerTwo=" + this.state.invitedPlayer,
                    this.props.apiConfig.headers)}>
                    <label>
                        Username of player to invite:
                        <input type="text" value={this.state.invitedPlayer} onChange={this.handleInvitedPlayerChange} />
                    </label>
                    <input type="submit" value="Submit" />
                </form>
                {inviteButtons}
                {successMessage}
            </div>


        )
    }

    handleInvitedPlayerChange(event) {
        event.preventDefault();
        this.setState({invitedPlayer: event.target.value});
    }


    async handleGeneralRequest(event, url, payload, headers){
        var self = this;
        event.preventDefault();
        this.props.handleGeneralRequest(event, url, payload, headers)
            .then(function(response) {
                    self.setState({
                        inviteSuccess: response.wasSuccessful
                    });
                self.props.updateInvites(response.invites)
                }
            )

    }
    async handleAcceptInvite(playerOne){
        console.log("playerOne is: " + playerOne);
        this.props.handleAcceptInvite(this.state.apiConfig.url, "action=AcceptInvite&playerOne=" + playerOne + "&username=" + this.props.username + "&password=" + this.props.password + "&playerTwo=" + this.props.username, this.props.apiConfig.headers)
            .then(response => this.props.updateInvites(response.invites));


    }
    async handleDeclineInvite(playerOne){

        this.props.handleAcceptInvite(this.state.apiConfig.url, "action=DeclineInvite&playerOne=" + playerOne + "&username=" + this.props.username + "&password=" + this.props.password + "&playerTwo=" + this.props.username, this.props.apiConfig.headers)
            .then(response => this.props.updateInvites(response.invites));

    }
}

export default Invite;