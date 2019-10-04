import React from "react";
import { Navbar, Nav, NavItem } from "react-bootstrap";

import Logout from "../Logout/Logout";

const HorizontalNavbar = (props) => {
    return (
        <Navbar bg="light" variant="light">

            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
                <Nav></Nav>
            </Navbar.Collapse>
            <div style={{paddingRight : "20px"}}>{props.user}</div>
            <Logout></Logout>
        </Navbar>
    );
};

export default HorizontalNavbar;
