import { fetchWithTimeout } from './common';

const url = '/api/v1/authentication';

const login = (username, password) => {
  return fetchWithTimeout(`${url}/login`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      email: username,
      password,
    }),
  }).then(response => {
    console.log('response', response);
    if (response.status !== 200) {
      return false;
    }
    return true;
  });
};

const logout = (username, password) => {
  return fetchWithTimeout(`${url}/logout`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
  }).then(response => {
    console.log('response', response);
    return true;
  });
};

export {
  login,
  logout,
};
