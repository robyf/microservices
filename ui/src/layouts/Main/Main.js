import React from 'react';
import PropTypes from 'prop-types';

import Container from 'react-bootstrap/Container';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { connect } from 'react-redux';
import { withRouter, Link } from 'react-router-dom';
import { LinkContainer } from 'react-router-bootstrap';

import { User } from '../../types';
import { logout } from '../../api/session';
import { logout as logoutAction } from '../../redux/actions';

import logo from '../../ac-full-logo.svg';

const Main = props => {
  const { children, history, logoutAction, user } = props;

  const doLogout = async () => {
    console.log('Log out');
    await logout();
    logoutAction();

    const { push } = history;
    push('/');
  }

  return (
    <Container>
      <Navbar bg="dark" variant="dark" expand="lg">
        <Navbar.Brand>
          <Link to="/dashboard">
            <img
              alt="Awesome Credit"
              src={logo}
              height="30"
              className="d-inline-block align-top"
            />
          </Link>
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="responsive-navbar-nav" />
        <Navbar.Collapse id="responsive-navbar-nav">
          <Nav className="mr-auto">
            <LinkContainer to="/dashboard">
              <Nav.Link>Dashboard</Nav.Link>
            </LinkContainer>
            <LinkContainer to="/events">
              <Nav.Link>Events</Nav.Link>
            </LinkContainer>
          </Nav>
          <Nav>
            <NavDropdown title={`${user.firstName} ${user.lastName}`} id="collapsible-nav-dropdown">
              <NavDropdown.Item onClick={() => doLogout()}>Log out</NavDropdown.Item>
            </NavDropdown>
          </Nav>
        </Navbar.Collapse>
      </Navbar>
      {children}
    </Container>
  );
};

Main.propTypes = {
  children: PropTypes.node,
  history: PropTypes.shape({
    push: PropTypes.func.isRequired,
  }).isRequired,
  logoutAction: PropTypes.func.isRequired,
  user: User.propTypes,
};

const mapStateToProps = state => {
  return {
    user: state.session.user,
  };
};

export default connect(mapStateToProps, { logoutAction })(withRouter(Main));
