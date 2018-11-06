import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import { Provider } from 'react-redux';
import * as serviceWorker from './serviceWorker';

// Material UI
import CssBaseline from '@material-ui/core/CssBaseline';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import blue from '@material-ui/core/colors/blue';
import pink from '@material-ui/core/colors/pink';

// import { http } from 'helpers/axios';
import store from 'store/createStore';

// Routes
import App from 'routes/App';
import Login from 'routes/Login';

import './polyfill';

// Custom theme
const theme = createMuiTheme({
  palette: {
    type: 'light',
    primary: {
      light: blue[300],
      main: blue[500],
      dark: blue[700],
    },
    secondary: {
      light: pink[300],
      main: pink[500],
      dark: pink[700],
    },
  },
  typography: {
    fontSize: 12,
    useNextVariants: true,
  },
});

// Use auth token if available
// http.interceptors.request.use(config => {
//   const { token } = store.getState().auth;
//   if (token) {
//     config.headers.authorization = token;
//     console.log('TOKEN: ', token);
//   }
//   return config;
// });

ReactDOM.render(
  <Provider store={store}>
    <Router>
      <React.Fragment>
        <MuiThemeProvider theme={theme}>
          <CssBaseline>
            <Route exact path="/" component={App} />
            <Route path="/login" component={Login} />
          </CssBaseline>
        </MuiThemeProvider>
      </React.Fragment>
    </Router>
  </Provider>,
  document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: http://bit.ly/CRA-PWA
serviceWorker.unregister();
