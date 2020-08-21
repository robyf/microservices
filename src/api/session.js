const url = '/api/v1/authentication'

const fetchWithTimeout = (requestUrl, params) => {
  return new Promise((resolve, reject) => {
    let didTimeout = false;
    const timeout = setTimeout(() => {
      didTimeout = true;
      reject(new Error('Request timed out'));
    }, 5000);

    fetch(requestUrl, params)
      .then(response => {
        clearTimeout(timeout);
        if (!didTimeout) {
          resolve(response);
        }
      })
      .catch(error => {
        if (didTimeout) {
          return;
        }
        reject(error);
      });
  });
};

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
