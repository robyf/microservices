import React from 'react';
import { Switch, Redirect } from 'react-router-dom';

import { RouteWithLayout } from './components';
import { Main as MainLayout } from './layouts';


function Home() {
  return <h2>Home</h2>;
}

function About() {
  return <h2>About</h2>;
}

function Users() {
  return <h2>Users</h2>;
}

function Login() {
  return <h2>Login</h2>;
}

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
        component={Home}
        exact
        layout={MainLayout}
        path="/dashboard"
      />
      <RouteWithLayout
        component={About}
        layout={MainLayout}
        path="/about"
        authenticated
      />
      <RouteWithLayout
        component={Users}
        layout={MainLayout}
        path="/users"
      />
      <RouteWithLayout
        component={Login}
        layout={MainLayout}
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
