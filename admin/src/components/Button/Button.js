import React from 'react';
import PropTypes from 'prop-types';
import styles from './Button.module.css';

const Button = ({ name, text, children, color, onClick }) => {
  console.log('STTLES: ', styles);

  const cor = () => {};

  return (
    <div>
      <button name={name} className={styles.button} onClick={onClick}>
        {children}
        {text}
      </button>
    </div>
  );
};
//https://github.com/css-modules/postcss-icss-values
Button.propTypes = {
  name: PropTypes.string,
  text: PropTypes.string,
};

export default Button;
