import React from 'react';
import PropTypes from 'prop-types';

import {
  BrowserRouter as Router,
} from "react-router-dom";
import { connect } from 'react-redux';

import 'bootstrap/dist/css/bootstrap.min.css';

import { currentUser } from './api/user';
import { setUser } from './redux/actions'
import Routes from './Routes';
import { Loading } from './components';

const App = ({ setUser }) => {

  const [loading, setLoading] = React.useState(true);

  React.useEffect(() => {
    const fetchData = async () => {
      const user = await currentUser();
      console.log('Fetched user: ', user);
      if (user) {
        console.log('x');
        setUser(user);
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

export default connect(null, { setUser })(App);
