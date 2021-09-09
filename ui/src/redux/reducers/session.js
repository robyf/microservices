import { LOGOUT, SET_USER, SET_ACCOUNT, SET_CREDIT_DECISION, SET_ACCOUNT_BALANCE } from '../actionTypes';

import { Account } from '../../types';

const initialState = {
  user: undefined,
  account: undefined,
  creditDecision: undefined,
};

export default function(state = initialState, action) {
  switch (action.type) {
    case LOGOUT:
      return initialState;
    case SET_USER:
      return {
        ...state,
        user: action.payload,
      };
    case SET_ACCOUNT:
      return {
        ...state,
        account: action.payload,
      };
    case SET_ACCOUNT_BALANCE:
      return {
        ...state,
        account: new Account(state.account, action.payload),
      };
    case SET_CREDIT_DECISION:
      return {
        ...state,
        creditDecision: action.payload,
      };
    default:
      return state;
  }
}
