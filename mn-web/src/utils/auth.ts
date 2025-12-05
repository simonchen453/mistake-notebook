export const TOKEN_KEY = 'mn_auth_token';
export const USER_KEY = 'mn_user_info';

export const setToken = (token: string) => localStorage.setItem(TOKEN_KEY, token);
export const getToken = () => localStorage.getItem(TOKEN_KEY);
export const removeToken = () => localStorage.removeItem(TOKEN_KEY);

export const setUser = (user: any) => localStorage.setItem(USER_KEY, JSON.stringify(user));
export const getUser = () => {
    const userStr = localStorage.getItem(USER_KEY);
    return userStr ? JSON.parse(userStr) : null;
};
export const removeUser = () => localStorage.removeItem(USER_KEY);
