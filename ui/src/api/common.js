const fetchWithTimeout = (requestUrl, params) => {
  return new Promise((resolve, reject) => {
    let didTimeout = false;
    const timeout = setTimeout(() => {
      didTimeout = true;
      reject(new Error("Request timed out"));
    }, 5000);

    fetch(requestUrl, params)
      .then((response) => {
        clearTimeout(timeout);
        if (!didTimeout) {
          resolve(response);
        }
      })
      .catch((error) => {
        if (didTimeout) {
          return;
        }
        reject(error);
      });
  });
};

const fetchWithTimeoutAndErrorHandling = (requestUrl, params) => {
  return new Promise((resolve, reject) => {
    fetchWithTimeout(requestUrl, params)
      .then(response => response.json())
      .then(response => {
        console.log("Response", response);
        if (response.errors) {
          response.errors.forEach(error => {
            console.log("Error", error);
            if (error.extensions?.type === 'problem' && error.extensions?.statusCode === 401) {
              reject(new UnauthorizedError(error.extensions?.detail));
            }
          });
        }
        resolve(response);
      })
      .catch(error => {
        reject(error)
      });
  })
};

class UnauthorizedError extends Error {
  constructor(message) {
    super(message);
    this.name = "UnauthorizedError";
  }
}

export {
  fetchWithTimeout,
  fetchWithTimeoutAndErrorHandling,
  UnauthorizedError,
};
