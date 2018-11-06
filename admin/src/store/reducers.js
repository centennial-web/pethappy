import { combineReducers } from 'redux';
import { reducer as auth } from './modules/auth';

// Todos os meus reducers sao combinados aqui
export const rootReducer = combineReducers({
  auth,
});

export default rootReducer;
