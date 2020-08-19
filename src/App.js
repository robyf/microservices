import React from 'react';

import {
  BrowserRouter as Router,
} from "react-router-dom";

import { currentUser } from './api/user';
import Routes from './Routes';
import { Loading } from './components';

function App() {

  const [loading, setLoading] = React.useState(true);

  React.useEffect(() => {
    const fetchData = async () => {
      const user = await currentUser();
      console.log('Fetched user: ', user);
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
}

export default App;
