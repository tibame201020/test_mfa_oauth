import axios, { AxiosRequestConfig, AxiosResponse } from 'axios';
import useAccountStore from '../stores/account-store'



const config: AxiosRequestConfig = {
  headers: {
    'Content-Type': 'application/json',
    'credentials': 'include'
  }
};

const axiosUtil = axios.create(config)

// pre request handle
axiosUtil.interceptors.request.use(
  (config) => {
    config.headers['Accept'] = 'application/json';
    const { access_token } = useAccountStore();
    if (access_token) {
      config.headers['Authorization'] = `Bearer ${access_token}`;
    }

    return config;
  },
  (error) => {

    console.error('Request Error Interceptor:', error);
    return Promise.reject(error);
  }
);

//pre handle response
axiosUtil.interceptors.response.use(
  (response: AxiosResponse) => {
    const access_token = response.headers['access_token'];
    const accountStore = useAccountStore();
    if (access_token) {
      accountStore.access_token = access_token
    }
    return response;
  },
  (error) => {
    console.error('Response Error Interceptor:', error);

    alert(error)
    return Promise.reject(error);
  }
);

export default axiosUtil