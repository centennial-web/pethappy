import React from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router';
import PropTypes from 'prop-types';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import FormControl from '@material-ui/core/FormControl';
import FormHelperText from '@material-ui/core/FormHelperText';
import Input from '@material-ui/core/Input';
import InputLabel from '@material-ui/core/InputLabel';
import LockIcon from '@material-ui/icons/LockOutlined';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import withStyles from '@material-ui/core/styles/withStyles';
import { Formik } from 'formik';
import * as yup from 'yup';
import { http } from 'helpers/axios';

import { loading, login, isAuth } from 'store/modules/auth';

const styles = theme => ({
  layout: {
    width: 'auto',
    display: 'block', // Fix IE 11 issue.
    marginLeft: theme.spacing.unit * 3,
    marginRight: theme.spacing.unit * 3,
    [theme.breakpoints.up(400 + theme.spacing.unit * 3 * 2)]: {
      width: 400,
      marginLeft: 'auto',
      marginRight: 'auto',
    },
  },
  paper: {
    marginTop: theme.spacing.unit * 8,
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    padding: `${theme.spacing.unit * 2}px ${theme.spacing.unit * 3}px ${theme
      .spacing.unit * 3}px`,
  },
  avatar: {
    margin: theme.spacing.unit,
    backgroundColor: theme.palette.secondary.main,
  },
  form: {
    width: '100%', // Fix IE 11 issue.
    marginTop: theme.spacing.unit,
  },
  submit: {
    marginTop: theme.spacing.unit * 3,
  },
});

const Login = props => {
  const { classes, location, isAuth, login } = props;

  const redirect = () => {
    if (location.state && location.state.nextPathname) {
      return location.state.nextPathname;
    } else {
      return '/'; // <- default
    }
  };

  const initialValues = {
    email: '',
    password: '',
  };

  const onSubmit = (values, actions) => {
    const auth = {
      username: values.email,
      password: values.password,
    };

    // Request token
    http
      .get('/api/login', { auth })
      .then(({ data }) => {
        // Log in
        login(data);
        // Say to formik we finish submitting the form
        actions.setSubmitting(false);
        actions.setStatus(null);
        // Return to promise
        return data;
      })
      .catch(error => {
        if (error.response) {
          if (error.response.status === 403) {
            actions.setStatus('Invalid credentials');
          } else {
            actions.setStatus(
              `Server respond with status code ${error.response.status}`
            );
          }
        } else if (error.request) {
          actions.setStatus('Server is not responding');
        } else {
          actions.setStatus(error.message);
        }
        actions.setSubmitting(false);
      });
  };

  const validationSchema = yup.object().shape({
    email: yup
      .string()
      .email('E-mail is not valid!')
      .required('E-mail is required!'),
    password: yup
      .string()
      .min(6, 'Password has to be longer than 6 characters!')
      .required('Password is required!'),
  });

  const LoginForm = ({
    values,
    errors,
    touched,
    handleBlur,
    handleChange,
    handleSubmit,
    isSubmitting,
    status,
  }) => (
    <form className={classes.form} onSubmit={handleSubmit}>
      <FormControl
        margin="normal"
        error={errors && errors.email && touched.email}
        required
        fullWidth
      >
        <InputLabel htmlFor="email">Email Address</InputLabel>
        <Input
          id="email"
          name="email"
          autoComplete="email"
          autoFocus
          onChange={handleChange}
          onBlur={handleBlur}
          value={values.email}
        />
        {errors &&
          errors.email &&
          touched.email && (
            <FormHelperText id="component-error-email">
              {errors.email}
            </FormHelperText>
          )}
      </FormControl>
      <FormControl
        margin="normal"
        required
        fullWidth
        error={errors && errors.password && touched.password}
      >
        <InputLabel htmlFor="password">Password</InputLabel>
        <Input
          name="password"
          type="password"
          id="password"
          autoComplete="current-password"
          onChange={handleChange}
          onBlur={handleBlur}
          value={values.password}
        />
        {errors &&
          errors.password &&
          touched.password && (
            <FormHelperText id="component-error-password">
              {errors.password}
            </FormHelperText>
          )}
      </FormControl>
      <Typography variant="body2" color="error">
        {status}
      </Typography>
      <Button
        type="submit"
        fullWidth
        variant="contained"
        color="primary"
        disabled={isSubmitting}
        className={classes.submit}
      >
        Login
      </Button>
    </form>
  );

  return (
    <React.Fragment>
      {isAuth && <Redirect to={redirect()} />}
      <main className={classes.layout}>
        <Paper className={classes.paper}>
          <Avatar className={classes.avatar}>
            <LockIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Pet Happy Admin
          </Typography>

          <Formik
            initialValues={initialValues}
            onSubmit={onSubmit}
            render={LoginForm}
            validationSchema={validationSchema}
          />
        </Paper>
      </main>
    </React.Fragment>
  );
};

Login.propTypes = {
  classes: PropTypes.object.isRequired,
};

const mapStateToProps = state => {
  return {
    isAuth: isAuth(state),
  };
};

const mapDispatchToProps = dispatch => {
  return {
    loading: () => {
      dispatch(loading);
    },
    login: token => {
      dispatch(login(token));
    },
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withStyles(styles)(Login));
