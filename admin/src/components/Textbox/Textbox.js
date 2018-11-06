import React from 'react';
import PropTypes from 'prop-types';

const Textbox = ({ name }) => {
  return (
    <div>
      <input type="text" name={name} />
    </div>
  );
};

Textbox.propTypes = {
  name: PropTypes.string,
};

export default Textbox;
