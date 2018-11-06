// export const roleSelector = state => _.get(state, 'auth.user.role', 'user');

// export const isRoot = createSelector(roleSelector, role => role === 'root');
// export const isAdmin = createSelector(roleSelector, role => role === 'admin');
// export const isUser = createSelector(roleSelector, role => role === 'user');

export const isAuth = state => {
  return state.auth && state.auth.email;
};
