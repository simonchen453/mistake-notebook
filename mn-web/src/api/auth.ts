import api from './axios';

export interface LoginRequest {
    userId: string;
    password: string;
    domain?: string;
    captcha?: string;
    captchaKey?: string;
}

export interface LoginResponse {
    id: string;
    userId: string;
    token: string;
    realName?: string;
    domain?: string;
    avatarUrl?: string;
    mobileNo?: string;
    deptNo?: string;
    deptName?: string;
}

/**
 * 用户登录
 * AdminPro 登录接口：POST /auth/login
 */
export const login = async (data: LoginRequest): Promise<LoginResponse> => {
    // AdminPro 默认 domain 为 'system'
    const loginData = {
        userId: data.userId,
        password: data.password,
        domain: data.domain || 'system',
        captcha: data.captcha,
        captchaKey: data.captchaKey,
    };
    return api.post('/auth/login', loginData);
};

/**
 * 用户登出
 */
export const logout = async (): Promise<void> => {
    return api.post('/auth/logout');
};

/**
 * 用户注册（如果支持）
 */
export const register = async (data: any) => {
    return api.post('/auth/register', data);
};
