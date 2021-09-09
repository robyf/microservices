import React from 'react';
import PropTypes from 'prop-types';

const Minimal = props => {
  const { children } = props;

  return (
    <div style={{width: '100%'}}>
      {children}
    </div>
  );
};

Minimal.propTypes = {
  children: PropTypes.node
};

export default Minimal;
