import React from 'react';
import PropTypes from 'prop-types';

import logo from '../../logo.svg';
import '../../App.css';

const Main = props => {
  const { children } = props;

  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          <code>MainLayout</code>
        </p>
        {children}
      </header>
    </div>
  );
};

Main.propTypes = {
  children: PropTypes.node
};

export default Main;
