import React, { Component } from 'react';
import styles from './App.module.css';

import Header from 'components/Header';
import Menu from 'components/Menu';

export class App extends Component {
  render = () => {
    return (
      <div className={styles.styles}>
        <Header />
        <Menu />
      </div>
    );
  };
}

export default App;
