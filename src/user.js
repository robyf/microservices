const url = '/api/graphql'

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

const currentUser = () => {
  return fetchWithTimeout(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/graphql',
    },
    body: `{ currentUser { id firstName lastName email } }`,
  }).then(response => response.json());
};

export {
  currentUser,
};
