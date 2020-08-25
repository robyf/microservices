import { Account, CreditDecision } from '../types';

const url = "/api/graphql";

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

const lendingAccount = () => {
  return fetchWithTimeout(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/graphql",
    },
    body: `{ lendingAccount { id status balance } }`,
  }).then((response) => response.json()).then((response => {
    if (response.data.lendingAccount) {
      return new Account(response.data.lendingAccount);
    } else {
      return null;
    }
  }));
};

const createLendingAccount = () => {
  return fetchWithTimeout(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/graphql",
    },
    body: `mutation { createLendingAccount { id status balance } }`,
  }).then((response) => response.json()).then((response => {
    if (response.data.createLendingAccount) {
      return new Account(response.data.createLendingAccount);
    } else {
      return null;
    }
  }));
};

const validCreditDecision = accountId => {
  return fetchWithTimeout(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/graphql",
    },
    body: `{ validCreditDecision(accountId:"${accountId}") { id status positive amount } }`,
  }).then((response) => response.json()).then((response => {
    if (response.data.validCreditDecision) {
      return new CreditDecision(response.data.validCreditDecision);
    } else {
      return null;
    }
  }));
};

const createCreditDecision = (accountId, income) => {
  return fetchWithTimeout(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/graphql",
    },
    body: `mutation { createCreditDecision(accountId:"${accountId}", income:${income}) { id status positive amount } }`,
  }).then((response) => response.json()).then((response => {
    console.log('Response', response);
    if (response.data.createCreditDecision) {
      return new CreditDecision(response.data.createCreditDecision);
    } else {
      return null;
    }
  }));
};

const acceptCreditDecision = (accountId, creditDecisionId) => {
  return fetchWithTimeout(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/graphql",
    },
    body: `mutation { acceptCreditDecision(accountId:"${accountId}", creditDecisionId:"${creditDecisionId}") { id status positive amount } }`,
  }).then((response) => response.json()).then((response => {
    console.log('Response', response);
    if (response.data.acceptCreditDecision) {
      return new CreditDecision(response.data.acceptCreditDecision);
    } else {
      return null;
    }
  }));
};

const withdraw = (accountId, amount) => {
  return fetchWithTimeout(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/graphql",
    },
    body: `mutation { withdraw(accountId:"${accountId}", amount:${amount}) { resultingBalance } }`,
  }).then((response) => response.json()).then((response => {
    console.log('Response', response);
    if (response.data.withdraw) {
      return response.data.withdraw.resultingBalance;
    } else {
      return null;
    }
  }));
};

const deposit = (accountId, amount) => {
  return fetchWithTimeout(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/graphql",
    },
    body: `mutation { deposit(accountId:"${accountId}", amount:${amount}) { resultingBalance } }`,
  }).then((response) => response.json()).then((response => {
    console.log('Response', response);
    if (response.data.deposit) {
      return response.data.deposit.resultingBalance;
    } else {
      return null;
    }
  }));
};

export {
  lendingAccount,
  createLendingAccount,
  validCreditDecision,
  createCreditDecision,
  acceptCreditDecision,
  withdraw,
  deposit,
};
