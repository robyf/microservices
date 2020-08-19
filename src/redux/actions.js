import { LOGOUT, SET_USER } from './actionTypes';

export const logout = () => ({
  type: LOGOUT,
});

export const setUser = user => ({
  type: SET_USER,
  payload: user,
});
