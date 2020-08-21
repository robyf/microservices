import { LOGOUT, SET_USER, SET_ACCOUNT } from '../actionTypes';

const initialState = {
  user: undefined,
  account: undefined,
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
    default:
      return state;
  }
}
