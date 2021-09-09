import { User } from '../types';

import { fetchWithTimeoutAndErrorHandling } from './common';

const url = "/api/graphql";

const currentUser = () => {
  return fetchWithTimeoutAndErrorHandling(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/graphql",
    },
    body: `{ currentUser { id firstName lastName email } }`,
  }).then((response => {
    if (response.data.currentUser) {
      return new User(response.data.currentUser);
    } else {
      return null;
    }
  }));
};

export { currentUser };
