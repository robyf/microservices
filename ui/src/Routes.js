import React from 'react';
import { Switch, Redirect } from 'react-router-dom';

import { RouteWithLayout } from './components';
import { Main as MainLayout } from './layouts';
import { Minimal as MinimalLayout } from './layouts';

import { Login as LoginView } from './views';
import { Dashboard as DashboardView } from './views';
import { Withdraw as WithdrawView } from './views';
import { Deposit as DepositView } from './views';
import { Events as EventsView } from './views';

function NotFound() {
  return <h2>404</h2>;
}

const Routes = () => {
  return (
    <Switch>
      <Redirect
        exact
        from="/"
        to="/dashboard"
      />
      <RouteWithLayout
        component={DashboardView}
        exact
        layout={MainLayout}
        path="/dashboard"
        authenticated
      />
      <RouteWithLayout
        component={WithdrawView}
        layout={MainLayout}
        path="/withdraw"
        authenticated
      />
      <RouteWithLayout
        component={DepositView}
        layout={MainLayout}
        path="/deposit"
        authenticated
      />
      <RouteWithLayout
        component={EventsView}
        layout={MainLayout}
        path="/events"
        authenticated
      />
      <RouteWithLayout
        component={LoginView}
        layout={MinimalLayout}
        path="/login"
      />
      <RouteWithLayout
        component={NotFound}
        exact
        layout={MainLayout}
        path="/not-found"
      />
      <Redirect to="/not-found" />
    </Switch>
  );
};

export default Routes;
