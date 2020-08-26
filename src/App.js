import React from 'react';
import PropTypes from 'prop-types';

import {
  BrowserRouter as Router,
} from "react-router-dom";
import { connect } from 'react-redux';

import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';

import { currentUser } from './api/user';
import { lendingAccount, validCreditDecision } from './api/account';
import { setUser, setAccount, setCreditDecision } from './redux/actions'
import Routes from './Routes';
import { Loading } from './components';

const App = ({ setUser, setAccount, setCreditDecision }) => {

  const [loading, setLoading] = React.useState(true);

  React.useEffect(() => {
    const fetchData = async () => {
      const user = await currentUser();
      if (user) {
        setUser(user);
        const account = await lendingAccount();
        if (account) {
          setAccount(account);
          const cd = await validCreditDecision();
          setCreditDecision(cd);
        }
      }
      setLoading(false);
    };
    fetchData();
  }, []);

  if (loading) {
    return (
      <Loading />
    );
  }

  return (
    <Router>
      <Routes />
    </Router>
  );
};

App.propTypes = {
  setUser: PropTypes.func.isRequired,
};

export default connect(null, { setUser, setAccount, setCreditDecision })(App);
