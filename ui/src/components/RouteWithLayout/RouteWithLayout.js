import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import { User } from '../../types';

const RouteWithLayout = props => {
  const { layout: Layout, component: Component, path, user, authenticated, ...rest } = props;

  if (authenticated && !user) {
    return <Redirect to="/login" />;
  }

  return (
    <Route
      {...rest}
      path={path}
      render={matchProps => (
        <Layout>
          <Component {...matchProps} />
        </Layout>
      )}
    />
  );
};

RouteWithLayout.propTypes = {
  component: PropTypes.any.isRequired,
  layout: PropTypes.any.isRequired,
  path: PropTypes.string,
  authenticated: PropTypes.bool,
  user: User.propTypes,
};

const mapStateToProps = state => {
  return {
    user: state.session.user,
  };
};

export default connect(mapStateToProps)(RouteWithLayout);
