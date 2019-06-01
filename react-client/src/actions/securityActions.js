import axios from "axios";
import { GET_ERRORS, SET_CURRENT_USER } from "./types";
import setJWTToken from "../securityUtils/setJWTToken";
import jwt_decode from "jwt-decode";

export const createNewUser = (new_user, history) => async dispatch => {
  try {
    await axios.post("/api/users/register", new_user);
    history.push("/login");
    dispatch({
      type: GET_ERRORS,
      payload: {}
    });
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data
    });
  }
};

export const login = login_request => async dispatch => {
  try {
    const res = await axios.post("/api/users/login", login_request);
    const { token } = res.data;

    localStorage.setItem("jwtToken", token);
    setJWTToken(token);
    const decoded_jwt = jwt_decode(token);

    dispatch({
      type: SET_CURRENT_USER,
      payload: decoded_jwt
    });
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data
    });
  }
};

export const logout = () => dispatch => {
  localStorage.removeItem("jwtToken");
  setJWTToken(false);

  dispatch({
    type: SET_CURRENT_USER,
    payload: {}
  });
};
