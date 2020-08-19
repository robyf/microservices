import React from 'react';

import {
  BrowserRouter as Router,
} from "react-router-dom";

import { currentUser } from './api/user';
import Routes from './Routes';

function App() {

  React.useEffect(() => {
    const fetchData = async () => {
      const user = await currentUser();
      console.log('Fetched user: ', user);
    };
    fetchData();
  }, []);

  return (
    <Router>
      <Routes />
    </Router>
  );
}

export default App;
