import { createStore, applyMiddleware } from 'redux';
import thunk from 'redux-thunk-fsa';
import promise from 'redux-promise';
import rootReducer from './reducers';

// Configura o middleware. Estou usando o reduz-thunk-fsa para poder
// "injetar" o dispatcher nas minhas actions.
// Permite que os meus actions creators retornem uma funcao em vez the
// uma action. A funcao interior recebe dispatch e getState como params.
// Por favor veja as actions que eu escrevi.
export const middleware = [
  thunk.withOpts({
    interrupt: true,
    next: true,
  }),
  promise,
];

// Instancia da store
const store = createStore(rootReducer, applyMiddleware(...middleware));

export default store;
