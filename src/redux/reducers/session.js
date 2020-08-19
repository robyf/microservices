import { LOGOUT, SET_USER } from '../actionTypes';

const initialState = {
  user: undefined,
  loggedIn: false,
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
    default:
      return state;
  }
}
