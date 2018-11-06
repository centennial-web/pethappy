import { createAction } from 'redux-actions';
import { http } from 'helpers/axios';

/**
 * Loading
 */
export const loading = createAction('@@auth/loading');

/**
 * Login
 */
export const login = createAction('@@auth/login', token => dispatch => {
  dispatch(loading());

  return http
    .get('/api/user', {
      headers: { Authorization: token },
    })
    .get('data');
});

/**
 * Logout
 */
export const logout = createAction('@@auth/logout');
