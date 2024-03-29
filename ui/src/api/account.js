import { Account, CreditDecision, Event } from '../types';

import { fetchWithTimeout, fetchWithTimeoutAndErrorHandling } from './common';

const url = "/api/graphql";

const lendingAccount = () => {
  return fetchWithTimeoutAndErrorHandling(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/graphql",
    },
    body: `{ lendingAccount { id status balance } }`,
  }).then((response => {
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

const validCreditDecision = () => {
  return fetchWithTimeoutAndErrorHandling(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/graphql",
    },
    body: `{ validCreditDecision { id status positive amount } }`,
  }).then((response => {
    if (response.data.validCreditDecision) {
      return new CreditDecision(response.data.validCreditDecision);
    } else {
      return null;
    }
  }));
};

const createCreditDecision = income => {
  return fetchWithTimeoutAndErrorHandling(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/graphql",
    },
    body: `mutation { createCreditDecision(income:${income}) { id status positive amount } }`,
  }).then((response => {
    console.log('Response', response);
    if (response.data.createCreditDecision) {
      return new CreditDecision(response.data.createCreditDecision);
    } else {
      return null;
    }
  }));
};

const acceptCreditDecision = creditDecisionId => {
  return fetchWithTimeoutAndErrorHandling(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/graphql",
    },
    body: `mutation { acceptCreditDecision(creditDecisionId:"${creditDecisionId}") { id status positive amount } }`,
  }).then((response => {
    console.log('Response', response);
    if (response.data.acceptCreditDecision) {
      return new CreditDecision(response.data.acceptCreditDecision);
    } else {
      return null;
    }
  }));
};

const withdraw = amount => {
  return fetchWithTimeoutAndErrorHandling(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/graphql",
    },
    body: `mutation { withdraw(amount:${amount}) { resultingBalance } }`,
  }).then((response => {
    console.log('Response', response);
    if (response.data.withdraw) {
      return response.data.withdraw.resultingBalance;
    } else {
      return null;
    }
  }));
};

const deposit = amount => {
  return fetchWithTimeoutAndErrorHandling(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/graphql",
    },
    body: `mutation { deposit(amount:${amount}) { resultingBalance } }`,
  }).then((response => {
    console.log('Response', response);
    if (response.data.deposit) {
      return response.data.deposit.resultingBalance;
    } else {
      return null;
    }
  }));
};

const accountEvents = () => {
  return fetchWithTimeoutAndErrorHandling(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/graphql",
    },
    body: `{ accountEvents { id time type amount resultingBalance } }`,
  }).then((response => {
    console.log('Response', response);
    if (response.data?.accountEvents) {
      return response.data.accountEvents.map(e => new Event(e));
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
  accountEvents,
};
