import { LOGOUT, SET_USER, SET_ACCOUNT } from './actionTypes';

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
