import React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';

import styles from './Menu.module.css';
import { logout } from 'store/modules/auth';

export const Menu = () => {
  return (
    <div className={styles.container}>
      <Link to="/login">Login</Link>
    </div>
  );
};

const mapStateToProps = state => {
  return {
    auth: state.auth,
  };
};

const mapDispatchToProps = dispatch => {
  return {
    logout: () => {
      dispatch(logout);
    },
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Menu);
