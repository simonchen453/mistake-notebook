import axios from 'axios';
import { getToken, removeToken, removeUser } from '../utils/auth';
import { message } from 'antd';

const api = axios.create({
    baseURL: '/api',
    timeout: 10000,
    withCredentials: true, // 支持 session 认证
    headers: {
        'X-Requested-With': 'XMLHttpRequest',
    },
});

api.interceptors.request.use(
    (config) => {
        const token = getToken();
        if (token) {
            // AdminPro 可能同时支持 token 和 session，都设置上
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
    (response) => {
        const { data } = response;
        
        // 检查业务错误码（AdminPro 格式）
        if (data.restCode && data.restCode !== '200' && data.restCode !== '0') {
            // 401 认证失败
            if (data.restCode === '401' || data.restCode === 401) {
                removeToken();
                removeUser();
                message.warning(data.message || '会话已过期，请重新登录');
                window.location.href = '/login';
                return Promise.reject({ response: { data }, isAuthError: true });
            }
            // 403 权限不足
            if (data.restCode === '403' || data.restCode === 403) {
                message.error(data.message || '权限不足');
                return Promise.reject({ response: { data }, isPermissionError: true });
            }
            // 其他错误
            return Promise.reject({ response: { data } });
        }
        
        return data;
    },
    (error) => {
        // HTTP 状态码错误
        if (error.response) {
            const { status, data } = error.response;
            if (status === 401) {
                removeToken();
                removeUser();
                message.warning('会话已过期，请重新登录');
                window.location.href = '/login';
            } else if (data?.message) {
                message.error(data.message);
            }
        } else {
            message.error('网络错误，请检查网络连接');
        }
        return Promise.reject(error);
    }
);

export default api;
