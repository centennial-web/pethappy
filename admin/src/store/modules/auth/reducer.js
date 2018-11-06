import { handleActions } from 'redux-actions';
import update from 'immutability-helper';
import cloneDeep from 'lodash/cloneDeep';

import { loading, login, logout } from './actions';

export const initialState = {
  loading: false,
  username: null,
  email: null,
  enabled: null,
  fullName: null,
  token: null,
  roles: [],
};

const resetedState = state => {
  return update(state, {
    loading: { $set: false },
    username: { $set: null },
    email: { $set: null },
    enabled: { $set: null },
    fullName: { $set: null },
    token: { $set: null },
    roles: [],
  });
};

export const reducer = handleActions(
  {
    [loading]: {
      next: state => update(state, { loading: { $set: true } }),
    },

    [login]: {
      next: (state, { payload }) => {
        // We're using the immutability-helper to create an immutable state.
        const newState = update(state, {
          loading: { $set: false },
          username: { $set: payload.username },
          email: { $set: payload.email },
          enabled: { $set: payload.enabled },
          fullName: { $set: payload.fullName },
          token: { $set: payload.token },
          roles: { $push: payload.authorities },
        });

        // Return the new state
        return newState;
      },
      throw: state => resetedState(state),
    },

    [logout]: {
      next: state => {
        // Clean the storage
        window.localStorage.clear();

        // Return an empty state
        return resetedState(state);
      },
      throw: state => resetedState(state),
    },
  },
  cloneDeep(initialState)
);
