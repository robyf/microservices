import React from 'react';
import PropTypes from 'prop-types';

import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';

import { login } from '../../api/session';
import { currentUser } from '../../api/user';
import { setUser } from '../../redux/actions';

import './Login.css';
import logo from '../../ac-logo.svg';

const Login = ({ setUser, history }) => {

  const [username, setUsername] = React.useState("");
  const [password, setPassword] = React.useState("");
  const [disabled, setDisabled] = React.useState(true);
  const [loading, setLoading] = React.useState(false);
  const [error, setError] = React.useState(false);

  const usernameChanged = event => {
    setUsername(event.target.value);
    validateForm(event.target.value, password);
  };

  const passwordChanged = event => {
    setPassword(event.target.value);
    validateForm(username, event.target.value);
  };

  const validateForm = (username, password) => {
    if (username !== "" && password !== "") {
      setDisabled(false);
    } else {
      setDisabled(true);
    }
  };

  const buttonClicked = async () => {
    console.log('Button clicked');
    setLoading(true);
    setError(false);
    if (!await login(username, password)) {
      console.log('Login failed');
      setError(true);
      setLoading(false);
    } else {
      const user = await currentUser();
      console.log('Fetched user: ', user);
      setUser(user);

      const { push } = history;
      push('/dashboard');
    }
  };

  return (
    <div className="text-center">
      <form className="form-signin">
        <img className="mb-4" src={logo} alt="Awesome Credit" width="72" height="72" />
        <h1 className="h3 mb-3 font-weight-normal">Please sign in</h1>
        {error && <Alert variant="danger">Login failed</Alert>}
        <label htmlFor="inputEmail" className="sr-only">Email address</label>
        <input value={username} onChange={event => usernameChanged(event)} type="email" id="inputEmail" className="form-control" placeholder="Email address" required autoFocus />
        <label htmlFor="inputPassword" className="sr-only">Password</label>
        <input value={password} onChange={event => passwordChanged(event)} type="password" id="inputPassword" className="form-control" placeholder="Password" required />
        <Button size="lg" variant="primary" block disabled={disabled || loading} onClick={() => buttonClicked()}>
          {loading ? 'Signing in...' : 'Sign in'}
        </Button>
      </form>
    </div>
  );
}

Login.propTypes = {
  setUser: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func.isRequired,
  }).isRequired,
};

export default connect(null, { setUser })(withRouter(Login));
