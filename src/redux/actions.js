import { LOGOUT, SET_USER, SET_ACCOUNT, SET_CREDIT_DECISION } from './actionTypes';

export const logout = () => ({
  type: LOGOUT,
});

export const setUser = user => ({
  type: SET_USER,
  payload: user,
});

export const setAccount = account => ({
  type: SET_ACCOUNT,
  payload: account,
});

export const setCreditDecision = cd => ({
  type: SET_CREDIT_DECISION,
  payload: cd,
});
